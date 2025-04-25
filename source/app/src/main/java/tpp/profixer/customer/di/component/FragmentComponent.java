package tpp.profixer.customer.di.component;


import tpp.profixer.customer.di.module.FragmentModule;
import tpp.profixer.customer.di.scope.FragmentScope;

import dagger.Component;
import tpp.profixer.customer.ui.fragment.home.HomeFragment;
import tpp.profixer.customer.ui.fragment.income.IncomeFragment;
import tpp.profixer.customer.ui.fragment.notification.NotificationFragment;
import tpp.profixer.customer.ui.fragment.profile.ProfileFragment;
import tpp.profixer.customer.ui.fragment.study.StudyFragment;

@FragmentScope
@Component(modules = {FragmentModule.class}, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(HomeFragment homeFragment);
    void inject(ProfileFragment profileFragment);
    void inject(IncomeFragment incomeFragment);
    void inject(NotificationFragment notificationFragment);

    void inject(StudyFragment studyFragment);
}
