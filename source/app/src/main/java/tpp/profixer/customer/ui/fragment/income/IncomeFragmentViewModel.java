package tpp.profixer.customer.ui.fragment.income;

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
import tpp.profixer.customer.ui.base.fragment.BaseFragmentViewModel;
import tpp.profixer.customer.utils.NetworkUtils;

public class IncomeFragmentViewModel extends BaseFragmentViewModel {
    public ObservableField<String> query = new ObservableField<>("");
    public Integer size = 10;
    public ObservableField<Integer> page = new ObservableField<>(0);
    public MutableLiveData<List<Course>> courses = new MutableLiveData<>();
    public ObservableField<Integer> totalPage = new ObservableField<>();
    public ObservableField<Boolean> isFree = new ObservableField<>();
    public ObservableField<Long> categoryId = new ObservableField<>();
    public ObservableField<Integer> totalElements = new ObservableField<>(0);
    public IncomeFragmentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }

    public void searchCourses(){
//        showLoading();
        compositeDisposable.add(repository.getApiService().searchCourses(query.get(),isFree.get(), categoryId.get(),size)
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
                            hideLoading();
                        }, throwable -> {
                            courses.setValue(new ArrayList<>());
                            hideLoading();
                            handleException(throwable);
                            Timber.e(throwable);
                        }));
    }
}
