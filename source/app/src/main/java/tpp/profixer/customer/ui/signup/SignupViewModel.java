package tpp.profixer.customer.ui.signup;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.request.SignupRequest;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class SignupViewModel extends BaseViewModel {

    public SignupRequest request = new SignupRequest();
    public ObservableField<Boolean> isShowPassword = new ObservableField<>(false);
    public ObservableField<Boolean> isShowPassword2 = new ObservableField<>(false);
    public SignupViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void doSignup(){
        showLoading();
        compositeDisposable.add(repository.getApiService().signup(request)
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
                            showSuccessMessage("Đăng kí thành công");
                            application.getCurrentActivity().finish();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void showPassword(){
        isShowPassword.set(Boolean.FALSE.equals(isShowPassword.get()));
    }
    public void showPassword2(){
        isShowPassword2.set(Boolean.FALSE.equals(isShowPassword2.get()));
    }
}
