package tpp.profixer.customer.ui.signup;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.Objects;

import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivitySignupBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;

public class SignupActivity extends BaseActivity<ActivitySignupBinding, SignupViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
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

        viewModel.isShowPassword2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int pos = viewBinding.editPassword2.getSelectionStart();
                if(!Objects.requireNonNull(viewModel.isShowPassword2.get())){
                    viewBinding.editPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.editPassword2.setTransformationMethod(null);
                }
                viewBinding.editPassword2.setSelection(pos);

            }
        });
    }
}
