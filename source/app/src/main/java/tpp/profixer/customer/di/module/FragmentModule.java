package tpp.profixer.customer.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.ViewModelProviderFactory;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.di.scope.FragmentScope;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import tpp.profixer.customer.ui.fragment.home.HomeFragmentViewModel;
import tpp.profixer.customer.ui.fragment.income.IncomeFragmentViewModel;
import tpp.profixer.customer.ui.fragment.notification.NotificationFragmentViewModel;
import tpp.profixer.customer.ui.fragment.profile.ProfileFragmentViewModel;
import tpp.profixer.customer.ui.fragment.study.StudyFragmentViewModel;

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

}
