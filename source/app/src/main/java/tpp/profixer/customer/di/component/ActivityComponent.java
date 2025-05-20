package tpp.profixer.customer.di.component;

import tpp.profixer.customer.di.module.ActivityModule;
import tpp.profixer.customer.di.scope.ActivityScope;
import tpp.profixer.customer.ui.account.AccountActivity;
import tpp.profixer.customer.ui.cart.CartActivity;
import tpp.profixer.customer.ui.category.CategoryActivity;
import tpp.profixer.customer.ui.changepassword.ChangePasswordActivity;
import tpp.profixer.customer.ui.course.CourseActivity;
import tpp.profixer.customer.ui.expert.ExpertActivity;
import tpp.profixer.customer.ui.home.HomeActivity;
import tpp.profixer.customer.ui.lesson.LessonActivity;
import tpp.profixer.customer.ui.login.LoginActivity;
import tpp.profixer.customer.ui.map.MapActivity;
import tpp.profixer.customer.ui.payment.PaymentActivity;
import tpp.profixer.customer.ui.signup.SignupActivity;
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

    void inject(AccountActivity accountActivity);

    void inject(CategoryActivity categoryActivity);

    void inject(SignupActivity signupActivity);

    void inject(ExpertActivity expertActivity);

    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(PaymentActivity paymentActivity);
}

