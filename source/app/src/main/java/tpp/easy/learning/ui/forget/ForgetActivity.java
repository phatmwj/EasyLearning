package tpp.easy.learning.ui.forget;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import tpp.easy.learning.BR;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.room.UserEntity;
import tpp.easy.learning.databinding.ActivityForgetBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.ui.base.activity.BaseActivity;
import tpp.easy.learning.ui.forget.ForgetViewModel;

public class ForgetActivity extends BaseActivity<ActivityForgetBinding, ForgetViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_forget;
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
        viewModel.request.setIdHash(getIntent().getStringExtra("IDHASH"));

        viewModel.request.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                switch (propertyId){
                    case BR.newPassword:
                        if(viewModel.request.getNewPassword() == null || viewModel.request.getNewPassword().isEmpty()){
                            viewBinding.layoutNewPass.setError("Mật khẩu mới không được để trống");
                            return;
                        }
                        if(viewModel.request.getNewPassword().length() < 6){
                            viewBinding.layoutNewPass.setError("Mật khẩu mới phải có ít nhất 6 ký tự");
                            return;
                        }
                        viewBinding.layoutNewPass.setError(null);
                        break;
                    case BR.otp:
                        if(viewModel.request.getOtp() == null || viewModel.request.getOtp().isEmpty()){
                            viewBinding.layoutPass.setError("Mã xác nhận không được để trống");
                            return;
                        }
                        viewBinding.layoutPass.setError(null);
                        break;
                    case BR.confirmPassword:
                        if(viewModel.request.getConfirmPassword() == null || viewModel.request.getConfirmPassword().isEmpty()){
                            viewBinding.layoutConfirmPass.setError("Xác nhận mật khẩu không được để trống");
                            return;
                        }
                        viewBinding.layoutConfirmPass.setError(null);
                        break;
                    default:
                        break;

                }
            }
        });

//        initView();
    }

    public void changePassword(){
        if(viewModel.request.getOtp() == null || viewModel.request.getOtp().isEmpty()){
            viewBinding.layoutPass.setError("Mã xác nhận không được để trống");
            return;
        }
        if(viewModel.request.getNewPassword() == null || viewModel.request.getNewPassword().isEmpty()){
            viewBinding.layoutNewPass.setError("Mật khẩu mới không được để trống");
            return;
        }
        if(viewModel.request.getConfirmPassword() == null || viewModel.request.getConfirmPassword().isEmpty()){
            viewBinding.layoutConfirmPass.setError("Xác nhận mật khẩu không được để trống");
            return;
        }

        if(!viewModel.request.getNewPassword().equals(viewModel.request.getConfirmPassword())){
            viewBinding.layoutConfirmPass.setError("Xác nhận mật khẩu không đúng");
            return;
        }
        viewModel.changePassword();
    }

}
