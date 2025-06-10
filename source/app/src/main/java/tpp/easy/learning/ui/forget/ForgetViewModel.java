package tpp.easy.learning.ui.forget;

import android.content.Intent;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.R;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.ApiModelUtils;
import tpp.easy.learning.data.model.api.ResponseWrapper;
import tpp.easy.learning.data.model.api.request.ForgetRequest;
import tpp.easy.learning.data.model.api.request.NewPassRequest;
import tpp.easy.learning.data.model.api.response.ForgetResponse;
import tpp.easy.learning.ui.base.activity.BaseViewModel;
import tpp.easy.learning.ui.email.EmailActivity;
import tpp.easy.learning.utils.NetworkUtils;

public class ForgetViewModel extends BaseViewModel {
    public NewPassRequest request = new NewPassRequest();
    public ForgetViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void changePassword(){
        showLoading();
        compositeDisposable.add(repository.getApiService().newPassword(request)
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
                            showSuccessMessage("Đổi mật khẩu thành công");
                            application.getCurrentActivity().finish();
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
                                    showErrorMessage(responseWrapper.getMessage());
                                }catch (Throwable error){
                                    Timber.d(error);
                                }
                            }else {
                                showErrorMessage(application.getString(R.string.network_error));
                            }
                        }));
    }
}
