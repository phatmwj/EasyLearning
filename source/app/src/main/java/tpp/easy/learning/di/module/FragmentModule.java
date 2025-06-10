package tpp.easy.learning.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.ViewModelProviderFactory;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.di.scope.FragmentScope;
import tpp.easy.learning.ui.base.fragment.BaseFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import tpp.easy.learning.ui.fragment.home.HomeFragmentViewModel;
import tpp.easy.learning.ui.fragment.income.IncomeFragmentViewModel;
import tpp.easy.learning.ui.fragment.notification.NotificationFragmentViewModel;
import tpp.easy.learning.ui.fragment.profile.ProfileFragmentViewModel;
import tpp.easy.learning.ui.fragment.study.StudyFragmentViewModel;
import tpp.easy.learning.ui.lesson.fragment.ContentViewModel;
import tpp.easy.learning.ui.lesson.fragment.IntroduceViewModel;
import tpp.easy.learning.ui.lesson.fragment.ReviewViewModel;

@Module
public class FragmentModule {

    private final BaseFragment<?, ?> fragment;

    public FragmentModule(BaseFragment<?, ?> fragment) {
        this.fragment = fragment;
    }

    @Named("access_token")
    @Provides
    @FragmentScope
    String provideToken(Repository repository) {
        return repository.getSharedPreferences().getToken();
    }

    @Provides
    @FragmentScope
    HomeFragmentViewModel provideHomeFragmentViewModel(Repository repository, Context application){
        Supplier<HomeFragmentViewModel> supplier = () -> new HomeFragmentViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<HomeFragmentViewModel> factory = new ViewModelProviderFactory<>(HomeFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(HomeFragmentViewModel.class);
    }

    @Provides
    @FragmentScope
    ProfileFragmentViewModel provideProfileFragmentViewModel(Repository repository, Context application){
        Supplier<ProfileFragmentViewModel> supplier = () -> new ProfileFragmentViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<ProfileFragmentViewModel> factory = new ViewModelProviderFactory<>(ProfileFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ProfileFragmentViewModel.class);
    }

    @Provides
    @FragmentScope
    IncomeFragmentViewModel provideIncomeFragmentViewModel(Repository repository, Context application){
        Supplier<IncomeFragmentViewModel> supplier = () -> new IncomeFragmentViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<IncomeFragmentViewModel> factory = new ViewModelProviderFactory<>(IncomeFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(IncomeFragmentViewModel.class);
    }

    @Provides
    @FragmentScope
    NotificationFragmentViewModel provideNotificationFragmentViewModel(Repository repository, Context application){
        Supplier<NotificationFragmentViewModel> supplier = () -> new NotificationFragmentViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<NotificationFragmentViewModel> factory = new ViewModelProviderFactory<>(NotificationFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(NotificationFragmentViewModel.class);
    }

    @Provides
    @FragmentScope
    StudyFragmentViewModel provideStudyFragmentViewModel(Repository repository, Context application){
        Supplier<StudyFragmentViewModel> supplier = () -> new StudyFragmentViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<StudyFragmentViewModel> factory = new ViewModelProviderFactory<>(StudyFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(StudyFragmentViewModel.class);
    }

    @Provides
    @FragmentScope
    ContentViewModel provideContentViewModel(Repository repository, Context application){
        Supplier<ContentViewModel> supplier = () -> new ContentViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<ContentViewModel> factory = new ViewModelProviderFactory<>(ContentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ContentViewModel.class);
    }

    @Provides
    @FragmentScope
    IntroduceViewModel provideIntroduceViewModel(Repository repository, Context application){
        Supplier<IntroduceViewModel> supplier = () -> new IntroduceViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<IntroduceViewModel> factory = new ViewModelProviderFactory<>(IntroduceViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(IntroduceViewModel.class);
    }

    @Provides
    @FragmentScope
    ReviewViewModel provideReviewViewModel(Repository repository, Context application){
        Supplier<ReviewViewModel> supplier = () -> new ReviewViewModel(repository, (ProFixerApplication) application);
        ViewModelProviderFactory<ReviewViewModel> factory = new ViewModelProviderFactory<>(ReviewViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ReviewViewModel.class);
    }

}
