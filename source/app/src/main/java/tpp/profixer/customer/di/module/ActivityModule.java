package tpp.profixer.customer.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.ViewModelProviderFactory;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.di.scope.ActivityScope;
import tpp.profixer.customer.ui.account.AccountViewModel;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.cart.CartViewModel;
import tpp.profixer.customer.ui.category.CategoryViewModel;
import tpp.profixer.customer.ui.changepassword.ChangePasswordViewModel;
import tpp.profixer.customer.ui.course.CourseViewModel;
import tpp.profixer.customer.ui.expert.ExpertViewModel;
import tpp.profixer.customer.ui.home.HomeViewModel;
import tpp.profixer.customer.ui.lesson.LessonViewModel;
import tpp.profixer.customer.ui.login.LoginViewModel;
import tpp.profixer.customer.ui.map.MapViewModel;
import tpp.profixer.customer.ui.signup.SignupViewModel;
import tpp.profixer.customer.ui.splash.SplashViewModel;
import tpp.profixer.customer.utils.GetInfo;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Named("access_token")
    @Provides
    @ActivityScope
    String provideToken(Repository repository) {
        return repository.getSharedPreferences().getToken();
    }

    @Named("device_id")
    @Provides
    @ActivityScope
    String provideDeviceId(Context applicationContext) {
        return GetInfo.getAll(applicationContext);
    }


    @Provides
    @ActivityScope
    SplashViewModel provideMainViewModel(Repository repository, Context application) {
        Supplier<SplashViewModel> supplier = () -> new SplashViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<SplashViewModel> factory = new ViewModelProviderFactory<>(SplashViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SplashViewModel.class);
    }

    @Provides
    @ActivityScope
    HomeViewModel provideHomeViewModel(Repository repository, Context application) {
        Supplier<HomeViewModel> supplier = () -> new HomeViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<HomeViewModel> factory = new ViewModelProviderFactory<>(HomeViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(HomeViewModel.class);
    }

    @Provides
    @ActivityScope
    MapViewModel provideMapViewModel(Repository repository, Context application) {
        Supplier<MapViewModel> supplier = () -> new MapViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<MapViewModel> factory = new ViewModelProviderFactory<>(MapViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MapViewModel.class);
    }

    @Provides
    @ActivityScope
    CourseViewModel provideCourseViewModel(Repository repository, Context application) {
        Supplier<CourseViewModel> supplier = () -> new CourseViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<CourseViewModel> factory = new ViewModelProviderFactory<>(CourseViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CourseViewModel.class);
    }

    @Provides
    @ActivityScope
    LoginViewModel provideLoginViewModel(Repository repository, Context application) {
        Supplier<LoginViewModel> supplier = () -> new LoginViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<LoginViewModel> factory = new ViewModelProviderFactory<>(LoginViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginViewModel.class);
    }

    @Provides
    @ActivityScope
    LessonViewModel provideLessonViewModel(Repository repository, Context application) {
        Supplier<LessonViewModel> supplier = () -> new LessonViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<LessonViewModel> factory = new ViewModelProviderFactory<>(LessonViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LessonViewModel.class);
    }

    @Provides
    @ActivityScope
    CartViewModel provideCartViewModel(Repository repository, Context application) {
        Supplier<CartViewModel> supplier = () -> new CartViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<CartViewModel> factory = new ViewModelProviderFactory<>(CartViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CartViewModel.class);
    }

    @Provides
    @ActivityScope
    AccountViewModel provideAccountViewModel(Repository repository, Context application) {
        Supplier<AccountViewModel> supplier = () -> new AccountViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<AccountViewModel> factory = new ViewModelProviderFactory<>(AccountViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AccountViewModel.class);
    }

    @Provides
    @ActivityScope
    CategoryViewModel provideCategoryViewModel(Repository repository, Context application) {
        Supplier<CategoryViewModel> supplier = () -> new CategoryViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<CategoryViewModel> factory = new ViewModelProviderFactory<>(CategoryViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CategoryViewModel.class);
    }

    @Provides
    @ActivityScope
    SignupViewModel provideSignupViewModel(Repository repository, Context application) {
        Supplier<SignupViewModel> supplier = () -> new SignupViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<SignupViewModel> factory = new ViewModelProviderFactory<>(SignupViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SignupViewModel.class);
    }

    @Provides
    @ActivityScope
    ExpertViewModel provideExpertViewModel(Repository repository, Context application) {
        Supplier<ExpertViewModel> supplier = () -> new ExpertViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<ExpertViewModel> factory = new ViewModelProviderFactory<>(ExpertViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ExpertViewModel.class);
    }

    @Provides
    @ActivityScope
    ChangePasswordViewModel provideChangePasswordViewModel(Repository repository, Context application) {
        Supplier<ChangePasswordViewModel> supplier = () -> new ChangePasswordViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<ChangePasswordViewModel> factory = new ViewModelProviderFactory<>(ChangePasswordViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ChangePasswordViewModel.class);
    }
}
