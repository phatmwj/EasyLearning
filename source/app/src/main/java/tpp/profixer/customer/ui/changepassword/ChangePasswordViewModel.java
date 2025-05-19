package tpp.profixer.customer.ui.changepassword;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.request.UpdateProfileRequest;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class ChangePasswordViewModel extends BaseViewModel {

    public UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
    public ObservableField<UpdateProfileRequest> currentProfile = new ObservableField<>();
    public Repository repository;
    public ObservableField<Boolean> isShowPassword = new ObservableField<>(false);
    public ObservableField<Boolean> isShowPassword2 = new ObservableField<>(false);
    public ObservableField<Boolean> isShowPassword3 = new ObservableField<>(false);
    public ChangePasswordViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
        this.repository = repository;
    }

    public void updateProfile(){
        showLoading();
        compositeDisposable.add(repository.getApiService().updateProfile(updateProfileRequest)
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
                            if(response.isResult()){
                                showSuccessMessage("Đổi mật khẩu thành công");
                                application.getCurrentActivity().finish();
                            }
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
    public void showPassword3(){
        isShowPassword3.set(Boolean.FALSE.equals(isShowPassword3.get()));
    }
}
