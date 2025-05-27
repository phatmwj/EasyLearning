package tpp.profixer.customer.ui.course;

import android.content.Intent;

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
import tpp.profixer.customer.data.model.api.request.RequestCourse;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.Review;
import tpp.profixer.customer.data.model.api.response.ReviewStar;
import tpp.profixer.customer.ui.base.BaseCallback;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;
import tpp.profixer.customer.ui.expert.ExpertActivity;
import tpp.profixer.customer.utils.NetworkUtils;

public class CourseViewModel extends BaseViewModel {

    public ObservableField<Course> course = new ObservableField<>();
    public Long categoryId;
    public Long courseId;

    //0: ko 1: trong gio hang, 2: đã mua
    public ObservableField<Integer> courseState = new ObservableField<>(0);
    public ObservableField<ReviewStar> reviewStar = new ObservableField<>();
    public MutableLiveData<List<Review>> reviewList = new MutableLiveData<>();
    public Integer pageReview = 0;
    public Integer sizeReview = 5;
    public MutableLiveData<List<Course>> relatedCourses = new MutableLiveData<>();
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

    public void getRelatedCourses(Long courseId, Long categoryId){
        List<Long> categoryIds = new ArrayList<>();
        List<Long> courseIds = new ArrayList<>();
        courseIds.add(courseId);
        categoryIds.add(categoryId);
        showLoading();
        compositeDisposable.add(repository.getApiService().getRelatedCourses(categoryIds, courseIds, 0, 5)
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

    public void addToCart(BaseCallback baseCallback){
        showLoading();
        compositeDisposable.add(repository.getApiService().addToCart(new RequestCourse(courseId))
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
                            hideLoading();
                            if(response.isResult()){
                                baseCallback.onSuccess();
                                getCart();
                            }else {
                                showErrorMessage(response.getMessage());
                            }
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                        }));
    }

    public void navigateToExpert(){
        Intent intent = new Intent(application.getCurrentActivity(), ExpertActivity.class);
        intent.putExtra("expert_id", course.get().getExpert().getId());
        application.getCurrentActivity().startActivity(intent);
    }
}
