package tpp.profixer.customer.ui.fragment.home;

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
import tpp.profixer.customer.data.model.api.response.CategoryCourse;
import tpp.profixer.customer.ui.base.fragment.BaseFragmentViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class HomeFragmentViewModel extends BaseFragmentViewModel {
    private Integer kind =1;
    private Integer status =1;
    public MutableLiveData<List<CategoryCourse>> categoryCourses = new MutableLiveData<>();
    public HomeFragmentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void getCategoryCourse(){
        showLoading();
        compositeDisposable.add(repository.getApiService().getCategories(kind,status)
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
                                categoryCourses.setValue(response.getData().getContent());
                                ProFixerApplication.categories.clear();
                                for (CategoryCourse categoryCourse : response.getData().getContent()) {
                                    ProFixerApplication.categories.add(categoryCourse.getCategory());
                                }
                            }else {
                                categoryCourses.setValue(new ArrayList<>());
                                Timber.e(response.getMessage());
                            }
                            hideLoading();
                        }, throwable -> {
                            categoryCourses.setValue(new ArrayList<>());
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }

}
