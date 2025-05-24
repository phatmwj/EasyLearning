package tpp.profixer.customer.ui.login;


import android.content.Intent;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.ApiModelUtils;
import tpp.profixer.customer.data.model.api.ResponseWrapper;
import tpp.profixer.customer.data.model.api.request.LoginRequest;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.ui.home.HomeActivity;
import tpp.profixer.customer.ui.signup.SignupActivity;
import tpp.profixer.customer.utils.NetworkUtils;

public class LoginViewModel extends BaseViewModel {
    public ObservableField<Boolean> isShowPassword = new ObservableField<>(false);
    public LoginRequest request = new LoginRequest();

    public ObservableField<String> tenantId = new ObservableField<>();

    public CompositeDisposable compositeDisposableUpdateDeviceToken = new CompositeDisposable();

    public LoginViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void doLogin(){
        showLoading();
        compositeDisposable.add(repository.getApiService().login(request)
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
                            showSuccessMessage("Đăng nhập thành công");
                            repository.getSharedPreferences().setToken(response.getAccess_token());
                            repository.getSharedPreferences().setUserId(response.getUser_id());
                            getProfile();
                            getCart2(response.getUser_id());
                            isLogin.set(true);
                        }, throwable -> {
                            hideLoading();
                            if (throwable instanceof HttpException) {
                                HttpException httpException = (HttpException) throwable;
                                if(httpException.code() >= 500){
                                    showErrorMessage(application.getString(R.string.server_error));
                                    return;
                                }
                                try {
                                    ResponseWrapper responseWrapper = ApiModelUtils.fromJson(httpException.response().errorBody().string(), ResponseWrapper.class);
                                    showErrorMessage("Số điện thoại hoặc mật khẩu không chính xác");
                                }catch (Throwable error){
                                    Timber.d(error);
                                }
                            }else {
                                showErrorMessage(application.getString(R.string.network_error));
                            }
                        }));
    }

    private void navigateToHome(){
        Intent intent = new Intent(application.getCurrentActivity(), HomeActivity.class);
        application.getCurrentActivity().startActivity(intent);
        application.getCurrentActivity().finish();
    }

    public void navigateToSignup(){
        Intent intent = new Intent(application.getCurrentActivity(), SignupActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public void showPassword(){
        isShowPassword.set(Boolean.FALSE.equals(isShowPassword.get()));
    }

    public void getCart2(Long userId){
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
                                ProFixerApplication.cartInfo = response.getData();
                                cartInfo.setValue(response.getData());
                            }
                            application.getCurrentActivity().finish();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                        }));
    }

}
