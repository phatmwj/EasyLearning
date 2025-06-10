package tpp.easy.learning.di.component;


import tpp.easy.learning.di.module.FragmentModule;
import tpp.easy.learning.di.scope.FragmentScope;

import dagger.Component;
import tpp.easy.learning.ui.fragment.home.HomeFragment;
import tpp.easy.learning.ui.fragment.income.IncomeFragment;
import tpp.easy.learning.ui.fragment.notification.NotificationFragment;
import tpp.easy.learning.ui.fragment.profile.ProfileFragment;
import tpp.easy.learning.ui.fragment.study.StudyFragment;
import tpp.easy.learning.ui.lesson.fragment.ContentFragment;
import tpp.easy.learning.ui.lesson.fragment.IntroduceFragment;
import tpp.easy.learning.ui.lesson.fragment.ReviewFragment;

@FragmentScope
@Component(modules = {FragmentModule.class}, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(HomeFragment homeFragment);
    void inject(ProfileFragment profileFragment);
    void inject(IncomeFragment incomeFragment);
    void inject(NotificationFragment notificationFragment);
    void inject(StudyFragment studyFragment);
    void inject(IntroduceFragment introduceFragment);
    void inject(ReviewFragment reviewFragment);
    void inject(ContentFragment contentFragment);
}
