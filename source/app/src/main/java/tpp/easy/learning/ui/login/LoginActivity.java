package tpp.easy.learning.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import java.util.Objects;
import tpp.easy.learning.BR;
import tpp.easy.learning.R;
import tpp.easy.learning.databinding.ActivityLoginBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.ui.base.activity.BaseActivity;
import tpp.easy.learning.ui.email.EmailActivity;


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

        viewModel.request.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.phone) {
                    String phone = viewModel.request.getPhone();
                    if (phone == null || phone.trim().isEmpty()) {
                        viewBinding.layoutUsername.setError("Số điện thoại hoặc email không được để trống");
                    } else {
                        viewBinding.layoutUsername.setError(null);
                    }
                }

                if (propertyId == BR.password) {
                    String password = viewModel.request.getPassword();
                    if (password == null || password.trim().isEmpty()) {
                        viewBinding.layoutPass.setError("Mật khẩu không được để trống");
                    } else {
                        viewBinding.layoutPass.setError(null);
                    }
                }
            }
        });

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

    public void login(){
        if(viewModel.request.getPhone() == null || viewModel.request.getPhone().isEmpty()){
            viewBinding.layoutUsername.setError("Số điện thoại hoặc email không được để trống");
            return;
        }else {
            viewBinding.layoutUsername.setError(null);
        }
        if(viewModel.request.getPassword() == null || viewModel.request.getPassword().isEmpty()){
            viewBinding.layoutPass.setError("Mật khẩu không được để trống");
            return;
        }else {
            viewBinding.layoutPass.setError(null);
        }
        viewModel.doLogin();
    }

    public void navigateToEmail(){
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
    }

}
