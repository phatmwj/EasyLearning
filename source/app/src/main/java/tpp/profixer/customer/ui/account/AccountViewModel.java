package tpp.profixer.customer.ui.account;

import androidx.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.request.UpdateProfileRequest;
import tpp.profixer.customer.data.model.api.response.Province;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class AccountViewModel extends BaseViewModel {

    public UpdateProfileRequest request = new UpdateProfileRequest();
    public ObservableField<UpdateProfileRequest> currentProfile = new ObservableField<>();
    public Repository repository;
    public ObservableField<Boolean> isShowPassword = new ObservableField<>(false);
    public ObservableField<List<Province>> provinces = new ObservableField<>();
    public ObservableField<List<Province>> districts = new ObservableField<>();
    public ObservableField<List<Province>> wards = new ObservableField<>();
    public AccountViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
        this.repository = repository;
    }

    public void showPassword(){
        isShowPassword.set(Boolean.FALSE.equals(isShowPassword.get()));
    }

    public void doUploadAvatar(MultipartBody.Part imagePart){
        RequestBody type = RequestBody.create("AVATAR", MediaType.parse("multipart/form-data"));
        compositeDisposable.add(repository.getApiService().uploadFile(type, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwable ->
                        throwable.flatMap((Function<Throwable, ObservableSource<?>>) throwable1 -> {
                            if (NetworkUtils.checkNetworkError(throwable1)) {
                                return application.showDialogNoInternetAccess();
                            }else{
                                return Observable.error(throwable1);
                            }
                        })
                )
                .subscribe(
                        response -> {
                            if(response.isResult()){

                            }else{
                                showErrorMessage(response.getMessage());
                            }
                            hideLoading();
                        },
                        throwable -> {
                            hideLoading();
                            showErrorMessage(throwable.getMessage());
                        }
                )
        );
    }


    public void getProvinces(Integer kink, Long parentId){
        compositeDisposable.add(repository.getApiService().getNation(kink, parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwable ->
                        throwable.flatMap((Function<Throwable, ObservableSource<?>>) throwable1 -> {
                            if (NetworkUtils.checkNetworkError(throwable1)) {
                                return application.showDialogNoInternetAccess();
                            }else{
                                return Observable.error(throwable1);
                            }
                        })
                )
                .subscribe(
                        response -> {
                            if(response.isResult() && response.getData().getContent() != null){
                                provinces.set(response.getData().getContent());
                            }else {
                                provinces.set(new ArrayList<>());
                            }
                            hideLoading();
                        },
                        throwable -> {
                            provinces.set(new ArrayList<>());
                            hideLoading();
                            showErrorMessage(throwable.getMessage());
                        }
                )
        );
    }

    public void getWards(Integer kink, Long parentId){
        compositeDisposable.add(repository.getApiService().getNation(kink, parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwable ->
                        throwable.flatMap((Function<Throwable, ObservableSource<?>>) throwable1 -> {
                            if (NetworkUtils.checkNetworkError(throwable1)) {
                                return application.showDialogNoInternetAccess();
                            }else{
                                return Observable.error(throwable1);
                            }
                        })
                )
                .subscribe(
                        response -> {
                            if(response.isResult() && response.getData().getContent() != null){
                                wards.set(response.getData().getContent());
                            }else {
                                wards.set(new ArrayList<>());
                            }
                            hideLoading();
                        },
                        throwable -> {
                            wards.set(new ArrayList<>());
                            hideLoading();
                            showErrorMessage(throwable.getMessage());
                        }
                )
        );
    }

    public void getDistricts(Integer kink, Long parentId){
        compositeDisposable.add(repository.getApiService().getNation(kink, parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwable ->
                        throwable.flatMap((Function<Throwable, ObservableSource<?>>) throwable1 -> {
                            if (NetworkUtils.checkNetworkError(throwable1)) {
                                return application.showDialogNoInternetAccess();
                            }else{
                                return Observable.error(throwable1);
                            }
                        })
                )
                .subscribe(
                        response -> {
                            if(response.isResult() && response.getData().getContent() != null){
                                districts.set(response.getData().getContent());
                            }else {
                                districts.set(new ArrayList<>());
                            }
                            hideLoading();
                        },
                        throwable -> {
                            districts.set(new ArrayList<>());
                            hideLoading();
                            showErrorMessage(throwable.getMessage());
                        }
                )
        );
    }

    public void updateProfile(){
        showLoading();
        compositeDisposable.add(repository.getApiService().updateProfile(request)
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
                            if(response.isResult()){
                                showSuccessMessage(response.getMessage());
                            }
                            getProfile();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
