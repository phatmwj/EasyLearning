package tpp.profixer.customer.ui.cart;

import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.response.CartInfo;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class CartViewModel extends BaseViewModel {
    public ObservableField<CartInfo> cartInfoF = new ObservableField<>();
    public MutableLiveData<List<Course>> relatedCourses = new MutableLiveData<>();
    public ObservableField<Integer> price = new ObservableField<>(0);
    public ObservableField<Integer> oldPrice = new ObservableField<>(0);
    public CartViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getRelatedCourses(List<Long> courseIds, List<Long> categoryIds){
//        showLoading();
        compositeDisposable.add(repository.getApiService().getRelatedCourses(categoryIds, courseIds,0, 5)
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
                                relatedCourses.setValue(response.getData().getContent());
                            }else {
                                relatedCourses.setValue(new ArrayList<>());
                                Timber.e(response.getMessage());
                            }
                            hideLoading();
                        }, throwable -> {
                            relatedCourses.setValue(new ArrayList<>());
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void deleteCartItem(Long cartItemId){
        showLoading();
        compositeDisposable.add(repository.getApiService().deleteCartItem(cartItemId)
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
                            getCart();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void deleteAllCartItem(){
        showLoading();
        compositeDisposable.add(repository.getApiService().deleteAllCartItem()
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
                            getCart();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
