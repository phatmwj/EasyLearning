package tpp.profixer.customer.ui.login;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import java.util.Objects;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityLoginBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;


public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }
    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        viewModel.isShowPassword.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int pos = viewBinding.editPassword.getSelectionStart();
                if(!Objects.requireNonNull(viewModel.isShowPassword.get())){
                    viewBinding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.editPassword.setTransformationMethod(null);
                }
                viewBinding.editPassword.setSelection(pos);

            }
        });
    }

}
