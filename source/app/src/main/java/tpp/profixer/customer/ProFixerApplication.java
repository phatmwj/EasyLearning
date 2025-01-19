package tpp.profixer.customer;

import static com.tinder.scarlet.websocket.okhttp.OkHttpClientUtils.newWebSocketFactory;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tinder.scarlet.Scarlet;
import com.tinder.scarlet.WebSocket;
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle;
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter;
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy;
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import tpp.profixer.customer.data.model.api.ApiModelUtils;
import tpp.profixer.customer.data.socket.Command;
import tpp.profixer.customer.data.socket.KittyRealtimeEvent;
import tpp.profixer.customer.data.socket.KittyService;
import tpp.profixer.customer.data.socket.dto.Message;
import tpp.profixer.customer.di.component.AppComponent;
import tpp.profixer.customer.di.component.DaggerAppComponent;
import tpp.profixer.customer.others.MyTimberDebugTree;
import tpp.profixer.customer.others.MyTimberReleaseTree;
import tpp.profixer.customer.utils.DialogUtils;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

public class ProFixerApplication extends Application implements LifecycleEventObserver {
    @Setter
    @Getter
    private AppCompatActivity currentActivity;

    @Getter
    private AppComponent appComponent;
    private Boolean inBackground;

    @Getter
    @Setter
    private Boolean paused = false;

    //websocket
    private Scarlet scarletInstance;
    private KittyService kittyService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Disposable disposableSocket;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(true);


        if (BuildConfig.DEBUG) {
            Timber.plant(new MyTimberDebugTree());
        } else {
            Timber.plant(new MyTimberReleaseTree(firebaseCrashlytics));
        }

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        appComponent.inject(this);

        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        createSocket();

    }


    public void createSocket(){
        createScarletInstance();
        createKittyService();
        if(compositeDisposable!=null){
            compositeDisposable.clear();
        }
        observeWebSocketEvent();
    }

    public void deleteSocket(){
        scarletInstance = null;
        kittyService = null;
        if(compositeDisposable!=null){
            compositeDisposable.clear();
        }
    }
    void createScarletInstance() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .writeTimeout(500, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        scarletInstance = new Scarlet.Builder()
                .webSocketFactory(newWebSocketFactory(okHttpClient, BuildConfig.WS_URL))
                .lifecycle(AndroidLifecycle.ofApplicationForeground(this))
                .addMessageAdapterFactory(new GsonMessageAdapter.Factory())
                .addStreamAdapterFactory(new RxJava2StreamAdapterFactory())
                .backoffStrategy(new ExponentialWithJitterBackoffStrategy(500L,1000L,new Random()))
                .build();
    }

    void createKittyService() {
        kittyService = scarletInstance.create(KittyService.class);
    }
    public void sendMessage(Message message){
        Timber.tag("SOCKET SEND").d("SEND: %s", ApiModelUtils.toJson(message));
        kittyService.request(message);
    }

    @SuppressLint("TimberArgCount")
    public void observeWebSocketEvent() {
        Flowable<WebSocket.Event> share = kittyService.observeWebSocketEvent()
                .filter(o -> !(o instanceof WebSocket.Event.OnMessageReceived))
                .observeOn(Schedulers.io())
                .share();

        compositeDisposable.add(share.subscribe(o -> {
            Timber.d(o.toString());
            KittyRealtimeEvent kittyRealtimeEvent = (KittyRealtimeEvent) currentActivity;
            if (kittyRealtimeEvent == null) return;
            if (o instanceof WebSocket.Event.OnConnectionOpened) {
                startPingSocket();
                kittyRealtimeEvent.onConnectionOpened();
            } else if (o instanceof WebSocket.Event.OnConnectionClosed) {
                stopPingSocket();
                kittyRealtimeEvent.onConnectionClosed();
            } else if (o instanceof WebSocket.Event.OnConnectionClosing) {
                kittyRealtimeEvent.onConnectionClosing();
            } else if (o instanceof WebSocket.Event.OnConnectionFailed) {
                stopPingSocket();
                kittyRealtimeEvent.onConnectionFailed();
            }
        }));

        compositeDisposable.add(kittyService.message()
                .observeOn(Schedulers.io())
                .subscribe(o -> {
                    Timber.tag("SOCKET RECEIVER").d("RECEIVER: %s", ApiModelUtils.toJson(o));
                    KittyRealtimeEvent kittyRealtimeEvent = (KittyRealtimeEvent) currentActivity;
                    if (kittyRealtimeEvent == null) return;
                    kittyRealtimeEvent.onMessageReceived(o);
                }));
    }

    private void stopPingSocket(){
        if(disposableSocket != null){
            compositeDisposable.remove(disposableSocket);
            disposableSocket = null;
        }
    }

    private void startPingSocket(){
        disposableSocket = Observable.interval(0,30,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(value -> {
                    sendClientPing();
                }, throwable -> {
                    Timber.e(throwable);
                });
        compositeDisposable.add(disposableSocket);
    }

    public void sendClientPing(){
        Message message = new Message();
        message.setCmd(Command.COMMAND_CLIENT_PING);
        message.setToken(appComponent.getRepository().getSharedPreferences().getToken());
        sendMessage(message);
    }

    public PublishSubject<Integer> showDialogNoInternetAccess() {
        final PublishSubject<Integer> subject = PublishSubject.create();
        currentActivity.runOnUiThread(() ->
                DialogUtils.dialogConfirm(currentActivity, currentActivity.getResources().getString(R.string.network_error),
                        currentActivity.getResources().getString(R.string.network_error_button_retry),
                        (dialogInterface, i) -> subject.onNext(1), currentActivity.getResources().getString(R.string.network_error_button_exit),
                        (dialogInterface, i) -> System.exit(0))
        );
        return subject;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                Timber.d("APP CREATE");
                break;
            case ON_START:
                inBackground = false;
                Timber.d("APP START");
                break;
            case ON_RESUME:
                Timber.d("APP RESUME");
                break;
            case ON_PAUSE:
                inBackground = true;
                Timber.d("APP PAUSE");
                break;
            case ON_STOP:
                Timber.d("APP STOP");
                break;
            case ON_DESTROY:
                Timber.d("APP DESTROY");
                break;
            default:
                Timber.d("APP ANY");
        }
    }
}
