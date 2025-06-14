package tpp.easy.learning.ui.base.fragment;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.Getter;
import retrofit2.HttpException;
import timber.log.Timber;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.R;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.ApiModelUtils;
import tpp.easy.learning.data.model.api.ResponseWrapper;
import tpp.easy.learning.data.model.other.ToastMessage;

import lombok.Setter;

public class BaseFragmentViewModel extends ViewModel {

    protected final Repository repository;
    @Getter
    protected final ProFixerApplication application;
    protected final MutableLiveData<ToastMessage> mErrorMessage = new MutableLiveData<>();
    protected final ObservableBoolean mIsLoading = new ObservableBoolean();
    public CompositeDisposable compositeDisposable;


    @Setter
    protected String token;

    public BaseFragmentViewModel(Repository repository, ProFixerApplication application) {
        this.repository = repository;
        this.application = application;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void showSuccessMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_SUCCESS, message));
    }

    public void showNormalMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_NORMAL, message));
    }

    public void showWarningMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_WARNING, message));
    }

    public void showErrorMessage(String message) {
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_ERROR, message));
    }

    public void showLoading() {
        mIsLoading.set(true);
    }

    public void hideLoading() {
        mIsLoading.set(false);
    }

    public void handleException(Throwable throwable){
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
    }
}
