package tpp.easy.learning.ui.lesson;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.api.request.CompleteLessonRequest;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.data.model.api.response.Lesson;
import tpp.easy.learning.ui.base.activity.BaseViewModel;
import tpp.easy.learning.utils.NetworkUtils;

public class LessonViewModel extends BaseViewModel {

    public ObservableField<Course> course = new ObservableField<>();
    public Long courseId;
    public Long expertId;
    public ObservableField<Boolean> isFullscreen = new ObservableField<>(false);
    public ObservableField<Lesson> currentLesson = new ObservableField<>();
    public LessonViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }
    public void getCourseDetails(){
        showLoading();
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

    public void getLessonDetails(Long lessonId){
//        showLoading();
        compositeDisposable.add(repository.getApiService().getLessonDetails(lessonId)
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
                                currentLesson.set(response.getData());
                            }
                            hideLoading();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void completeLesson(Integer secondProgress){
        compositeDisposable.add(repository.getApiService().completeLesson(new CompleteLessonRequest(courseId, currentLesson.get().getId(), secondProgress))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
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
                        }, throwable -> {
                            handleException(throwable);
                        }));
    }

    public void finishedLesson(){
        compositeDisposable.add(repository.getApiService().finishedLesson(new CompleteLessonRequest(courseId, currentLesson.get().getId(),null))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
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
                        }, throwable -> {
                            handleException(throwable);
                        }));
    }
}
