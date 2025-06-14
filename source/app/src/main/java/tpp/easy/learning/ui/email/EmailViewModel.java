package tpp.easy.learning.ui.email;

import android.content.Intent;

import androidx.databinding.ObservableField;

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
import tpp.easy.learning.data.model.api.response.ForgetResponse;
import tpp.easy.learning.ui.base.activity.BaseViewModel;
import tpp.easy.learning.ui.forget.ForgetActivity;
import tpp.easy.learning.utils.NetworkUtils;

public class EmailViewModel extends BaseViewModel {
    public ObservableField<String> email = new ObservableField<>();
    public EmailViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void requestForgetPassword(){
        ForgetRequest request = new ForgetRequest();
        request.setEmail(email.get());
        showLoading();
        compositeDisposable.add(repository.getApiService().requestForgetPassword(request)
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
                            showSuccessMessage("Mã xác nhận đã được gửi đến email của bạn");
                            ForgetResponse forgetResponse = response.getData();
                            Intent intent = new Intent(application.getCurrentActivity(), ForgetActivity.class);
                            intent.putExtra("IDHASH", forgetResponse.getIdHash());
                            application.getCurrentActivity().startActivity(intent);
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
                                    showErrorMessage("Email không chính xác");
                                }catch (Throwable error){
                                    Timber.d(error);
                                }
                            }else {
                                showErrorMessage(application.getString(R.string.network_error));
                            }
                        }));
    }
}
