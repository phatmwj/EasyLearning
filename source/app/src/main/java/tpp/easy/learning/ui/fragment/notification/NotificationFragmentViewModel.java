package tpp.easy.learning.ui.fragment.notification;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.response.Notification;
import tpp.easy.learning.ui.base.fragment.BaseFragmentViewModel;
import tpp.easy.learning.utils.NetworkUtils;

public class NotificationFragmentViewModel extends BaseFragmentViewModel {

    public ObservableField<Integer> state = new ObservableField<>();
    public ObservableField<Integer> totalNoti = new ObservableField<>(-1);
    public MutableLiveData<List<Notification>> notification = new MutableLiveData<>();
    public NotificationFragmentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getNotification(){
        showLoading();
        compositeDisposable.add(repository.getApiService().getNotification(1, state.get())
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
                            if(response.isResult() && response.getData() != null && response.getData().getContent() != null){
                                notification.setValue(response.getData().getContent());
                                totalNoti.set(response.getData().getTotalElements());
                            }else {
                                notification.setValue(new ArrayList<>());
                                totalNoti.set(0);
                            }
                            hideLoading();
                        }, throwable -> {
                            notification.setValue(new ArrayList<>());
                            totalNoti.set(0);
                            hideLoading();
                            handleException(throwable);
                        }));
    }

    public void readAll(){
        showLoading();
        compositeDisposable.add(repository.getApiService().readAll(1)
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
                            showSuccessMessage(response.getMessage());
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                        }));
    }

    public void deleteAll(){
        showLoading();
        compositeDisposable.add(repository.getApiService().deleteAllNotification(1)
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
                            showSuccessMessage(response.getMessage());
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                        }));
    }

}
