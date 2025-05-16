package tpp.profixer.customer.ui.expert;

import android.text.Html;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.ApiModelUtils;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.ExpertInfo;
import tpp.profixer.customer.data.model.api.response.Identification;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class ExpertViewModel extends BaseViewModel {

    public ObservableField<ExpertInfo> expertInfo = new ObservableField<>();
    public ObservableField<Identification> identification = new ObservableField<>();
    public Long expertId;
    public MutableLiveData<List<Course>> relatedCourses = new MutableLiveData<>();
    public ExpertViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getExpertInfo(){
        showLoading();
        compositeDisposable.add(repository.getApiService().getExpertInfo(expertId)
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
                            expertInfo.set(response.getData());
                            identification.set(ApiModelUtils.fromJson(expertInfo.get().getIdentification(), Identification.class));
//                            hideLoading();
                            getCourses();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void getCourses(){
//        showLoading();
        compositeDisposable.add(repository.getApiService().getCoursesByExpert(expertId)
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
                            if(response.isResult() && response.getData() != null && response.getData().getContent() != null){
                                relatedCourses.setValue(response.getData().getContent());
                            }else {
                                relatedCourses.setValue(new ArrayList<>());
                                Timber.e(response.getMessage());
                            }
                            hideLoading();
                        }, throwable -> {
                            relatedCourses.setValue(new ArrayList<>());
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
