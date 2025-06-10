package tpp.easy.learning.ui.lesson.fragment;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.ui.base.fragment.BaseFragmentViewModel;
import tpp.easy.learning.utils.NetworkUtils;

public class ContentViewModel extends BaseFragmentViewModel {
    public ContentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public ObservableField<Course> course = new ObservableField<>();
    public void getCourseDetails(Long courseId){
//        showLoading();
        compositeDisposable.add(repository.getApiService().getCourseDetails(courseId)
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
                            if(response.isResult() && response.getData() != null){
                                course.set(response.getData());
                            }
                            hideLoading();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
