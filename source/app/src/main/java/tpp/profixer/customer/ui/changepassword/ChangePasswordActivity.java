package tpp.profixer.customer.ui.changepassword;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.request.UpdateProfileRequest;
import tpp.profixer.customer.data.model.room.UserEntity;
import tpp.profixer.customer.databinding.ActivityChangepasswordBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;

public class ChangePasswordActivity extends BaseActivity<ActivityChangepasswordBinding, ChangePasswordViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_changepassword;
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
        viewModel.repository.getRoomService().userDao().loadAllToLiveData().observe(this, userEntities -> {
            Long userId = viewModel.repository.getSharedPreferences().getUserId();
            for (UserEntity userEntity : userEntities) {
                if (userEntity.getId().equals(userId)){
                    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
                    updateProfileRequest.setFullName(userEntity.getFullName());
                    updateProfileRequest.setPhone(userEntity.getPhone());
                    updateProfileRequest.setEmail(userEntity.getEmail());
                    updateProfileRequest.setProvinceId(userEntity.getProvinceId());
                    updateProfileRequest.setWardId(userEntity.getWardId());
                    updateProfileRequest.setDistrictId(userEntity.getDistrictId());
                    updateProfileRequest.setAddress(userEntity.getAddress());
                    viewModel.updateProfileRequest = updateProfileRequest;
                }
            }
        });

        viewModel.updateProfileRequest.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                switch (propertyId){
                    case BR.newPassword:
                        if(viewModel.updateProfileRequest.getNewPassword() == null || viewModel.updateProfileRequest.getNewPassword().isEmpty()){
                            viewBinding.layoutNewPass.setError("Mật khẩu mới không được để trống");
                            return;
                        }
                        if(viewModel.updateProfileRequest.getNewPassword().length() < 6){
                            viewBinding.layoutNewPass.setError("Mật khẩu mới phải có ít nhất 6 ký tự");
                            return;
                        }
                        viewBinding.layoutNewPass.setError(null);
                        break;
                    case BR.oldPassword:
                        if(viewModel.updateProfileRequest.getOldPassword() == null || viewModel.updateProfileRequest.getOldPassword().isEmpty()){
                            viewBinding.layoutPass.setError("Mật khẩu hiện tại không được để trống");
                            return;
                        }
                        viewBinding.layoutPass.setError(null);
                        break;
                    case BR.confirmPassword:
                        if(viewModel.updateProfileRequest.getConfirmPassword() == null || viewModel.updateProfileRequest.getConfirmPassword().isEmpty()){
                            viewBinding.layoutConfirmPass.setError("Xác nhận mật khẩu không được để trống");
                            return;
                        }
                        if(!viewModel.updateProfileRequest.getNewPassword().equals(viewModel.updateProfileRequest.getConfirmPassword())){
                            viewBinding.layoutConfirmPass.setError("Xác nhận mật khẩu không đúng");
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

    public void updateProfile(){
        if(viewModel.updateProfileRequest.getOldPassword() == null || viewModel.updateProfileRequest.getOldPassword().isEmpty()){
            viewBinding.layoutPass.setError("Mật khẩu hiện tại không được để trống");
            return;
        }
        if(viewModel.updateProfileRequest.getNewPassword() == null || viewModel.updateProfileRequest.getNewPassword().isEmpty()){
            viewBinding.layoutNewPass.setError("Mật khẩu mới không được để trống");
            return;
        }
        if(viewModel.updateProfileRequest.getConfirmPassword() == null || viewModel.updateProfileRequest.getConfirmPassword().isEmpty()){
            viewBinding.layoutConfirmPass.setError("Xác nhận mật khẩu không được để trống");
            return;
        }
        viewModel.updateProfile();
    }


//    private void initView() {
//        viewModel.isShowPassword.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                int pos = viewBinding.editPassword.getSelectionStart();
//                if(!Objects.requireNonNull(viewModel.isShowPassword.get())){
//                    viewBinding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }else {
//                    viewBinding.editPassword.setTransformationMethod(null);
//                }
//                viewBinding.editPassword.setSelection(pos);
//
//            }
//        });
//
//        viewModel.isShowPassword2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                int pos = viewBinding.editPassword2.getSelectionStart();
//                if(!Objects.requireNonNull(viewModel.isShowPassword2.get())){
//                    viewBinding.editPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }else {
//                    viewBinding.editPassword2.setTransformationMethod(null);
//                }
//                viewBinding.editPassword2.setSelection(pos);
//
//            }
//        });
//
//        viewModel.isShowPassword3.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                int pos = viewBinding.editPassword3.getSelectionStart();
//                if(!Objects.requireNonNull(viewModel.isShowPassword3.get())){
//                    viewBinding.editPassword3.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }else {
//                    viewBinding.editPassword3.setTransformationMethod(null);
//                }
//                viewBinding.editPassword3.setSelection(pos);
//
//            }
//        });
//    }
}
