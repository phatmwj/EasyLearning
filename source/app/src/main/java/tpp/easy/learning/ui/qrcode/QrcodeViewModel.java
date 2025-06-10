package tpp.easy.learning.ui.qrcode;

import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.request.BankInfo;
import tpp.easy.learning.data.model.api.request.BookingRequest;
import tpp.easy.learning.data.model.api.response.BookingResponse;
import tpp.easy.learning.data.model.api.response.DeepLink;
import tpp.easy.learning.data.model.api.response.PaymentInfo;
import tpp.easy.learning.ui.base.activity.BaseViewModel;
import tpp.easy.learning.utils.NetworkUtils;

public class QrcodeViewModel extends BaseViewModel {

    public ObservableField<PaymentInfo> paymentInfo = new ObservableField<>();
    public ObservableField<String> qrCode = new ObservableField<>();
    public ObservableField<String> timeString = new ObservableField<>();
    public ObservableField<String> status = new ObservableField<>("");
    public ObservableField<List<DeepLink>> deepLinks = new ObservableField<>(new ArrayList<>());
    public ObservableField<DeepLink> cDeepLink = new ObservableField<>();
    public ObservableField<BookingResponse> booking = new ObservableField<>();
    public QrcodeViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
        getDeepLinkBank();
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
                            paymentInfo.set(response.getData());
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
                            status.set(response.getData().getStatus());
                        }, throwable -> {
                            Timber.e(throwable);
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
                            qrCode.set(response.getData().getQrDataURL());
                            hideLoading();
                        }, throwable -> {
                            Timber.e(throwable);
                            hideLoading();
                            handleException(throwable);
                        }));
    }

    public void getDeepLinkBank(){
        compositeDisposable.add(repository.getApiService().getDeepLinks()
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
                            deepLinks.set(response.getApps());
                        }, throwable -> {
                            Timber.e(throwable);
                            handleException(throwable);
                        }));
    }

    public void getBooking(){
        showLoading();
        compositeDisposable.add(repository.getApiService().getBooking(paymentInfo.get().getData().getOrderCode())
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
                            booking.set(response.getData());
                        }, throwable -> {
                            hideLoading();
                            Timber.e(throwable);
                            handleException(throwable);
                        }));
    }
}
