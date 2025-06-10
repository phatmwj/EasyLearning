package tpp.easy.learning.ui.category;

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
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.ui.base.activity.BaseViewModel;
import tpp.easy.learning.utils.NetworkUtils;

public class CategoryViewModel extends BaseViewModel {
    public Long categoryId;
    public ObservableField<String> title = new ObservableField<>("");
    public Integer size = 10;
    public ObservableField<Integer> page = new ObservableField<>(0);
    public MutableLiveData<List<Course>> courses = new MutableLiveData<>();
    public ObservableField<Integer> totalPage = new ObservableField<>();
    public MutableLiveData<List<Course>> freeCourses = new MutableLiveData<>();
    public ObservableField<Integer> totalFreeCourses = new ObservableField<>(0);
    public CategoryViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getCoursesByCategory(){
        showLoading();
        compositeDisposable.add(repository.getApiService().getCoursesByCategory(categoryId, page.get(), size, null)
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
                                courses.setValue(response.getData().getContent());
                                totalPage.set(response.getData().getTotalPages());
                            }else {
                                courses.setValue(new ArrayList<>());
                                Timber.e(response.getMessage());
                            }
                            getFreeCoursesByCategory();
                        }, throwable -> {
                            courses.setValue(new ArrayList<>());
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void getFreeCoursesByCategory(){
        compositeDisposable.add(repository.getApiService().getCoursesByCategory(categoryId, page.get(), Integer.MAX_VALUE, true)
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
                                freeCourses.setValue(response.getData().getContent());
                            }else {
                                freeCourses.setValue(new ArrayList<>());
                                Timber.e(response.getMessage());
                            }
                            hideLoading();
                        }, throwable -> {
                            freeCourses.setValue(new ArrayList<>());
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
