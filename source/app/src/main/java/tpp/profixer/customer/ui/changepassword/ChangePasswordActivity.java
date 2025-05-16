package tpp.profixer.customer.ui.changepassword;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import java.util.Objects;

import io.github.glailton.expandabletextview.BR;
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
                    viewModel.updateProfileRequest.set(updateProfileRequest);
                }
            }
        });

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

        viewModel.isShowPassword3.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int pos = viewBinding.editPassword3.getSelectionStart();
                if(!Objects.requireNonNull(viewModel.isShowPassword3.get())){
                    viewBinding.editPassword3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.editPassword3.setTransformationMethod(null);
                }
                viewBinding.editPassword3.setSelection(pos);

            }
        });
    }
}
