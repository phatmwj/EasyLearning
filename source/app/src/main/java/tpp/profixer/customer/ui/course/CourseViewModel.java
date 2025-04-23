package tpp.profixer.customer.ui.course;

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
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.Review;
import tpp.profixer.customer.data.model.api.response.ReviewStar;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class CourseViewModel extends BaseViewModel {

    public ObservableField<Course> course = new ObservableField<>();
    public ObservableField<ReviewStar> reviewStar = new ObservableField<>();
    public MutableLiveData<List<Review>> reviewList = new MutableLiveData<>();
    public Integer pageReview = 0;
    public Integer sizeReview = 5;
    public CourseViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getCourseDetails(Long courseId){
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

    public void getReviewStar(Long courseId){
        showLoading();
        compositeDisposable.add(repository.getApiService().getReviewStar(courseId)
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
                                reviewStar.set(response.getData());
                            }
                            hideLoading();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void getReviewList(Long courseId){
        showLoading();
        compositeDisposable.add(repository.getApiService().getReviewList(courseId, pageReview, sizeReview)
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
                                reviewList.setValue(response.getData().getContent());
                            }else {
                                reviewList.setValue(new ArrayList<>());
                                Timber.e(response.getMessage());
                            }
                            hideLoading();
                        }, throwable -> {
                            reviewList.setValue(new ArrayList<>());
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
