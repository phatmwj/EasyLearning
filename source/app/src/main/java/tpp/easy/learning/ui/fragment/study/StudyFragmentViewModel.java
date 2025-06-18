package tpp.easy.learning.ui.fragment.study;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.ui.base.fragment.BaseFragmentViewModel;
import tpp.easy.learning.utils.NetworkUtils;

public class StudyFragmentViewModel extends BaseFragmentViewModel {

    public ObservableField<Boolean> isFinished = new ObservableField<>(false);
    public ObservableField<Integer> totalCourse = new ObservableField<>(-1);
    public MutableLiveData<List<Course>> courses = new MutableLiveData<>();
    public StudyFragmentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getMyCourse(){
        compositeDisposable.add(repository.getApiService().getMyCourses(isFinished.get())
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
                                totalCourse.set(response.getData().getTotalElements());
                            }else {
                                courses.setValue(new ArrayList<>());
                                totalCourse.set(0);
                            }
                            hideLoading();
                        }, throwable -> {
                            courses.setValue(new ArrayList<>());
                            totalCourse.set(0);
                            hideLoading();
                            handleException(throwable);
                        }));
    }
}
