package tpp.profixer.customer.ui.payment;

import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.ApiModelUtils;
import tpp.profixer.customer.data.model.api.request.BookingRequest;
import tpp.profixer.customer.data.model.api.response.CartInfo;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.ui.qrcode.QrcodeActivity;
import tpp.profixer.customer.utils.NetworkUtils;

public class PaymentViewModel extends BaseViewModel {
//    public MutableLiveData<CartInfo> cartInfo = new MutableLiveData<>();
    public ObservableField<Integer> price = new ObservableField<>(0);
    public ObservableField<Integer> oldPrice = new ObservableField<>(0);
    public ObservableField<Integer> voucher = new ObservableField<>(0);
    public ObservableField<CartInfo> cartInfoF = new ObservableField<>();
    public PaymentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void createBooking(){
        showLoading();
        compositeDisposable.add(repository.getApiService().createBooking(new BookingRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwable ->
                        throwable.flatMap((Function<Throwable, ObservableSource<?>>) throwable1 -> {
                            if (NetworkUtils.checkNetworkError(throwable1)) {
                                hideLoading();
                                return application.showDialogNoInternetAccess();
                            }else{
                                return Observable.error(throwable1);
                            }
                        })
                )
                .subscribe(
                        response -> {
                            hideLoading();
                            Timber.e(response.getData().getData().getCheckoutUrl());
                            Intent intent = new Intent(application.getCurrentActivity(), QrcodeActivity.class);
                            intent.putExtra("payment_info", ApiModelUtils.GSON.toJson(response.getData()));
                            application.getCurrentActivity().startActivity(intent);
                            application.getCurrentActivity().finish();
                            getCart();
                        }, throwable -> {
                            Timber.e(throwable);
                            hideLoading();
                            handleException(throwable);
                        }));
    }
}
