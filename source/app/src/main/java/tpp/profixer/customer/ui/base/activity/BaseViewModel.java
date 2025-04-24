package tpp.profixer.customer.ui.base.activity;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.ApiModelUtils;
import tpp.profixer.customer.data.model.api.ResponseWrapper;
import tpp.profixer.customer.data.model.api.response.Cart;
import tpp.profixer.customer.data.model.api.response.CartInfo;
import tpp.profixer.customer.data.model.other.ToastMessage;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.Getter;
import lombok.Setter;
import tpp.profixer.customer.utils.NetworkUtils;

public class BaseViewModel extends ViewModel {
    protected final ObservableBoolean mIsLoading = new ObservableBoolean();
    protected final MutableLiveData<String> progressBarMsg = new MutableLiveData<>();
    protected final MutableLiveData<ToastMessage> mErrorMessage = new MutableLiveData<>();
    protected final Repository repository;
    @Getter
    protected final ProFixerApplication application;
    public CompositeDisposable compositeDisposable;
    @Setter
    protected String token;
    @Setter
    protected String deviceId;

    public MutableLiveData<CartInfo> cartInfo = new MutableLiveData<>();

    public BaseViewModel(Repository repository, ProFixerApplication application) {
        this.repository = repository;
        this.application = application;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void showLoading() {
        mIsLoading.set(true);
    }

    public void hideLoading() {
        mIsLoading.set(false);
    }

    public void showSuccessMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_SUCCESS, message));
    }

    public void showNormalMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_NORMAL, message));
    }

    public void showWarningMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_WARNING, message));
    }

    public void showErrorMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_ERROR, message));
    }

    public void changeProgressBarMsg(String message) {
        progressBarMsg.setValue(message);
    }

    public void handleException(Throwable throwable){
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            if(httpException.code() >= 500){
                showErrorMessage(application.getString(R.string.server_error));
                return;
            }
            try {
                ResponseWrapper responseWrapper = ApiModelUtils.fromJson(httpException.response().errorBody().string(), ResponseWrapper.class);
                showErrorMessage(responseWrapper.getMessage());
            }catch (Throwable error){
                Timber.d(error);
            }
        }else {
            showErrorMessage(application.getString(R.string.network_error));
        }
    }

    public void getProfile (){
        Long userId = repository.getSharedPreferences().getUserId();
        if(userId == null || userId == -1){
            return;
        }
        showLoading();
        compositeDisposable.add(repository.getApiService().getProfile(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .retryWhen(throwable ->
                        throwable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Throwable {
                                if (NetworkUtils.checkNetworkError(throwable)) {
                                    hideLoading();
                                    return application.showDialogNoInternetAccess();
                                }else{
                                    return Observable.error(throwable);
                                }
                            }
                        })
                )
                .subscribe(
                        response -> {
                            hideLoading();
                            if(response.isResult() && response.getData() != null){
                                repository.getRoomService().userDao().insert(response.getData().getAccount().convertToEntity());
                            }
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                        }));
    }

    public void getCart(){
        Long userId = repository.getSharedPreferences().getUserId();
        if(userId == null || userId == -1 || token == null || token.equals("NULL")){
            return;
        }
//        showLoading();
        compositeDisposable.add(repository.getApiService().getCart(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwable ->
                        throwable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Throwable {
                                if (NetworkUtils.checkNetworkError(throwable)) {
                                    hideLoading();
                                    return application.showDialogNoInternetAccess();
                                }else{
                                    return Observable.error(throwable);
                                }
                            }
                        })
                )
                .subscribe(
                        response -> {
                            hideLoading();
                            if(response.isResult() && response.getData() != null){
                                cartInfo.setValue(response.getData());
                            }
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                        }));
    }
}
