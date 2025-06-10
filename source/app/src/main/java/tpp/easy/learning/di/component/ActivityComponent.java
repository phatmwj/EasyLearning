package tpp.easy.learning.di.component;

import tpp.easy.learning.di.module.ActivityModule;
import tpp.easy.learning.di.scope.ActivityScope;
import tpp.easy.learning.ui.account.AccountActivity;
import tpp.easy.learning.ui.cart.CartActivity;
import tpp.easy.learning.ui.category.CategoryActivity;
import tpp.easy.learning.ui.changepassword.ChangePasswordActivity;
import tpp.easy.learning.ui.course.CourseActivity;
import tpp.easy.learning.ui.email.EmailActivity;
import tpp.easy.learning.ui.expert.ExpertActivity;
import tpp.easy.learning.ui.home.HomeActivity;
import tpp.easy.learning.ui.lesson.LessonActivity;
import tpp.easy.learning.ui.login.LoginActivity;
import tpp.easy.learning.ui.map.MapActivity;
import tpp.easy.learning.ui.payment.PaymentActivity;
import tpp.easy.learning.ui.qrcode.QrcodeActivity;
import tpp.easy.learning.ui.signup.SignupActivity;
import tpp.easy.learning.ui.splash.SplashActivity;

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

    void inject(EmailActivity emailActivity);

    void inject(QrcodeActivity qrcodeActivity);
}

