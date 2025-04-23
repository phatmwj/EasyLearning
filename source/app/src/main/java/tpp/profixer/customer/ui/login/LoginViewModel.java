package tpp.profixer.customer.ui.login;


import android.content.Intent;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.request.LoginRequest;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.ui.home.HomeActivity;
import tpp.profixer.customer.utils.NetworkUtils;

public class LoginViewModel extends BaseViewModel {
    public ObservableField<Boolean> isShowPassword = new ObservableField<>(false);
    public ObservableField<LoginRequest> request = new ObservableField<>(new LoginRequest());

    public ObservableField<String> tenantId = new ObservableField<>();

    public CompositeDisposable compositeDisposableUpdateDeviceToken = new CompositeDisposable();

    public LoginViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void doLogin(){
        showLoading();
        compositeDisposable.add(repository.getApiService().login(request.get())
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
                            repository.getSharedPreferences().setToken(response.getAccess_token());
                            showSuccessMessage("Đăng nhập thành công");
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    private void navigateToHome(){
        Intent intent = new Intent(application.getCurrentActivity(), HomeActivity.class);
        application.getCurrentActivity().startActivity(intent);
        application.getCurrentActivity().finish();
    }

    public void showPassword(){
        isShowPassword.set(Boolean.FALSE.equals(isShowPassword.get()));
    }

}
