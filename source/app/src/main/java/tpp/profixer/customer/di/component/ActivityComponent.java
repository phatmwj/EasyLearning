package tpp.profixer.customer.di.component;

import tpp.profixer.customer.di.module.ActivityModule;
import tpp.profixer.customer.di.scope.ActivityScope;
import tpp.profixer.customer.ui.cart.CartActivity;
import tpp.profixer.customer.ui.course.CourseActivity;
import tpp.profixer.customer.ui.home.HomeActivity;
import tpp.profixer.customer.ui.lesson.LessonActivity;
import tpp.profixer.customer.ui.login.LoginActivity;
import tpp.profixer.customer.ui.map.MapActivity;
import tpp.profixer.customer.ui.splash.SplashActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(HomeActivity homeActivity);
    void inject(MapActivity mapActivity);

    void inject(CourseActivity courseActivity);

    void inject(LoginActivity loginActivity);

    void inject(LessonActivity lessonActivity);

    void inject(CartActivity cartActivity);
}

