package tpp.profixer.customer.ui.qrcode;

import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.request.BankInfo;
import tpp.profixer.customer.data.model.api.request.BookingRequest;
import tpp.profixer.customer.data.model.api.response.PaymentInfo;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class QrcodeViewModel extends BaseViewModel {

    public ObservableField<PaymentInfo> paymentInfo = new ObservableField<>();
    public ObservableField<String> qrCode = new ObservableField<>();
    public QrcodeViewModel(Repository repository, ProFixerApplication application) {
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
                            checkStatus(response.getData().getData().getPaymentLinkId());
                            BankInfo request = new BankInfo();
                            request.setAccountNo(response.getData().getData().getAccountNumber());
                            request.setAccountName(response.getData().getData().getAccountName());
                            request.setAmount(String.valueOf(response.getData().getData().getAmount()));
                            request.setAddInfo(response.getData().getData().getDescription());
                            generateQrcode(request);
//                            Timber.e(response.getData().getData().getCheckoutUrl());
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(response.getData().getData().getCheckoutUrl()));
//                            application.getCurrentActivity().startActivity(intent);
                            paymentInfo.set(response.getData());
                        }, throwable -> {
                            Timber.e(throwable);
                            hideLoading();
                            handleException(throwable);
                        }));
    }

    public void checkStatus(String paymentLinkId){
        compositeDisposable.add(repository.getApiService().checkStatus(paymentLinkId)
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
                        }, throwable -> {
                            Timber.e(throwable);
                            hideLoading();
                            handleException(throwable);
                        }));
    }

public void generateQrcode(BankInfo request){
        compositeDisposable.add(repository.getApiService().generateQrcode(request)
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
                            qrCode.set(response.getData().getQrDataURL());
                        }, throwable -> {
                            Timber.e(throwable);
                            hideLoading();
                            handleException(throwable);
                        }));
    }
}
