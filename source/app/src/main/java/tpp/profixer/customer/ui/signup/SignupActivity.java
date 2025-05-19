package tpp.profixer.customer.ui.signup;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.Objects;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.request.SignupRequest;
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

        viewModel.request.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                SignupRequest request = viewModel.request;

                switch (propertyId) {
                    case BR.fullName:
                        if (request.getFullName() == null || request.getFullName().isEmpty()) {
                            viewBinding.layoutFullname.setError("Họ và tên không được để trống");
                        } else {
                            viewBinding.layoutFullname.setError(null);
                        }
                        break;

                    case BR.email:
                        if (request.getEmail() == null || request.getEmail().isEmpty()) {
                            viewBinding.layoutEmail.setError("Email không được để trống");
                        } else {
                            viewBinding.layoutEmail.setError(null);
                        }
                        break;

                    case BR.phone:
                        if (request.getPhone() == null || request.getPhone().isEmpty()) {
                            viewBinding.layoutPhone.setError("Số điện thoại không được để trống");
                        } else if (request.getPhone().length() != 10) {
                            viewBinding.layoutPhone.setError("Số điện thoại không hợp lệ");
                        } else {
                            viewBinding.layoutPhone.setError(null);
                        }
                        break;

                    case BR.password:
                        if (request.getPassword() == null || request.getPassword().isEmpty()) {
                            viewBinding.layoutPass.setError("Mật khẩu không được để trống");
                        } else if (request.getPassword().length() < 6) {
                            viewBinding.layoutPass.setError("Mật khẩu phải có ít nhất 6 ký tự");
                        } else {
                            viewBinding.layoutPass.setError(null);
                        }
                        break;

                    case BR.confirmPassword:
                        if (request.getConfirmPassword() == null || request.getConfirmPassword().isEmpty()) {
                            viewBinding.layoutPassConfirm.setError("Xác nhận mật khẩu không được để trống");
                        } else if (!request.getConfirmPassword().equals(request.getPassword())) {
                            viewBinding.layoutPassConfirm.setError("Xác nhận mật khẩu không đúng");
                        } else {
                            viewBinding.layoutPassConfirm.setError(null);
                        }
                        break;
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

    public void signup(){
        if(viewModel.request.getFullName() == null || viewModel.request.getFullName().isEmpty()){
            viewBinding.layoutFullname.setError("Họ và tên không được để trống");
            return;
        }else {
            viewBinding.layoutFullname.setError(null);
        }
        if(viewModel.request.getEmail() == null || viewModel.request.getEmail().isEmpty()){
            viewBinding.layoutEmail.setError("Email không được để trống");
            return;
        }else {
            viewBinding.layoutEmail.setError(null);
        }
        if(viewModel.request.getPhone() == null || viewModel.request.getPhone().isEmpty()){
            viewBinding.layoutPhone.setError("Số điện thoại không được để trống");
            return;
        }else {
            viewBinding.layoutPhone.setError(null);
        }
        if(viewModel.request.getPhone().length() != 10){
            viewBinding.layoutPhone.setError("Số điện thoại không hợp lệ");
            return;
        }else {
            viewBinding.layoutPhone.setError(null);
        }
        if(viewModel.request.getPassword() == null || viewModel.request.getPassword().isEmpty()){
            viewBinding.layoutPass.setError("Mật khẩu không được để trống");
            return;
        }else {
            viewBinding.layoutPass.setError(null);
        }
        if(viewModel.request.getPassword().length() < 6){
            viewBinding.layoutPass.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }else {
            viewBinding.layoutPass.setError(null);
        }
        if(viewModel.request.getConfirmPassword() == null || viewModel.request.getConfirmPassword().isEmpty()){
            viewBinding.layoutPassConfirm.setError("Xác nhận mật khẩu không được để trống");
            return;
        }else {
            viewBinding.layoutPassConfirm.setError(null);
        }
        if(!viewModel.request.getPassword().equals(viewModel.request.getConfirmPassword())){
            viewBinding.layoutPassConfirm.setError("Xác nhận mật khẩu không đúng");
            return;
        }else {
            viewBinding.layoutPassConfirm.setError(null);
        }
        viewModel.doSignup();

    }
}
