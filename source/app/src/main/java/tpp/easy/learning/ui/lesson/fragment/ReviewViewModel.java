package tpp.easy.learning.ui.lesson.fragment;

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
import tpp.easy.learning.data.model.api.request.ReviewRequest;
import tpp.easy.learning.data.model.api.response.Review;
import tpp.easy.learning.ui.base.fragment.BaseFragmentViewModel;
import tpp.easy.learning.ui.dialog.ReviewDialog;
import tpp.easy.learning.utils.NetworkUtils;

public class ReviewViewModel extends BaseFragmentViewModel {

    public MutableLiveData<List<Review>> reviewList = new MutableLiveData<>();
    public Integer pageReview = 0;
    public Integer sizeReview = Integer.MAX_VALUE;
    public Long courseId;
    public Long expertId;
    ReviewDialog reviewDialog;
    public ReviewViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getReviewList(Long courseId){
//        showLoading();
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

    public void getMyReview(){
        showLoading();
        compositeDisposable.add(repository.getApiService().getMyReview(courseId)
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
                            reviewDialog = new ReviewDialog(application.getCurrentActivity());
                            if(response.isResult() && response.getData() != null && response.getData().getContent() != null && !response.getData().getContent().isEmpty()){
                                reviewDialog.star.set(response.getData().getContent().get(0).getStar());
                                reviewDialog.msg.set(response.getData().getContent().get(0).getMessage());
                                reviewDialog.reviewed.set(true);
                            }
                            reviewDialog.setListener(new ReviewDialog.ReviewListener() {
                                @Override
                                public void onReview(Integer star, String msg) {
                                    review(star,msg);
                                }

                                @Override
                                public void onNoStart() {
                                    showErrorMessage("Vui lòng đánh giá trước khi gửi");
                                }
                            });
                            reviewDialog.show();
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

    public void review(Integer star, String message){
        ReviewRequest request = new ReviewRequest();
        request.setKind(1);
        request.setStar(star);
        request.setMessage(message);
        request.setCourseId(courseId);
        request.setStudentId(repository.getSharedPreferences().getUserId());
        request.setExpertId(expertId);
        showLoading();
        compositeDisposable.add(repository.getApiService().review(request)
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
                                reviewDialog.dismiss();
                                showSuccessMessage(response.getMessage());
                                getReviewList(courseId);
                            }
                        }, throwable -> {
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
