package tpp.profixer.customer.ui.account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;
import tpp.profixer.customer.R;
import tpp.profixer.customer.constant.Constants;
import tpp.profixer.customer.data.model.api.request.SignupRequest;
import tpp.profixer.customer.data.model.api.request.UpdateProfileRequest;
import tpp.profixer.customer.data.model.room.UserEntity;
import tpp.profixer.customer.databinding.ActivityAccountBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.utils.RealPathUtil;

public class AccountActivity extends BaseActivity<ActivityAccountBinding, AccountViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_account;
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

        viewModel.provinces.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.provinces.get() != null) {
                    if(viewModel.request.getProvinceId() == null){
                        viewBinding.actvPaymentMethod.setText("",false);
                    }
                    List<String> paymentMethods = new ArrayList<>();
                    for (int i = 0; i < viewModel.provinces.get().size(); i++) {
                        paymentMethods.add(viewModel.provinces.get().get(i).getName());
                        if(viewModel.request.getProvinceId() != null && viewModel.request.getProvinceId().equals(viewModel.provinces.get().get(i).getId())){
                            viewBinding.actvPaymentMethod.setText(viewModel.provinces.get().get(i).getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AccountActivity.this,
                            R.layout.item_payment_method,
                            R.id.tv_payment_name,
                            paymentMethods
                    );
                    viewBinding.actvPaymentMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            viewBinding.actvPaymentMethod.setText(paymentMethods.get(position), false);
                            viewModel.request.setProvinceId(viewModel.provinces.get().get(position).getId());
                            viewModel.request.setDistrictId(null);
                            viewModel.getDistricts(2, viewModel.request.getProvinceId());
                            viewModel.request.setWardId(null);
                            viewModel.wards.set(new ArrayList<>());
                        }
                    });
                    viewBinding.actvPaymentMethod.setAdapter(adapter);
                }
            }
        });

        viewModel.districts.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.districts.get() != null) {
                    if(viewModel.request.getDistrictId() == null){
                        viewBinding.actvPaymentMethod2.setText("", false);
                    }
                    List<String> paymentMethods = new ArrayList<>();
                    for (int i = 0; i < viewModel.districts.get().size(); i++) {
                        paymentMethods.add(viewModel.districts.get().get(i).getName());
                        if(viewModel.request.getDistrictId() != null && viewModel.request.getDistrictId().equals(viewModel.districts.get().get(i).getId())){
                            viewBinding.actvPaymentMethod2.setText(viewModel.districts.get().get(i).getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AccountActivity.this,
                            R.layout.item_payment_method,
                            R.id.tv_payment_name,
                            paymentMethods
                    );
                    viewBinding.actvPaymentMethod2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            viewBinding.actvPaymentMethod2.setText(paymentMethods.get(position),false);
                            viewModel.request.setDistrictId(viewModel.districts.get().get(position).getId());
                            viewModel.request.setWardId(null);
                            viewModel.getWards(3, viewModel.request.getDistrictId());
                        }
                    });
                    viewBinding.actvPaymentMethod2.setAdapter(adapter);
                }
            }
        });

        viewModel.wards.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.wards.get() != null) {
                    if(viewModel.request.getWardId() == null){
                        viewBinding.actvPaymentMethod3.setText("",false);
                    }
                    List<String> paymentMethods = new ArrayList<>();
                    for (int i = 0; i < viewModel.wards.get().size(); i++) {
                        paymentMethods.add(viewModel.wards.get().get(i).getName());
                        if(viewModel.request.getWardId() != null && viewModel.request.getWardId().equals(viewModel.wards.get().get(i).getId())){
                            viewBinding.actvPaymentMethod3.setText(viewModel.wards.get().get(i).getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AccountActivity.this,
                            R.layout.item_payment_method,
                            R.id.tv_payment_name,
                            paymentMethods
                    );
                    viewBinding.actvPaymentMethod3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            viewBinding.actvPaymentMethod3.setText(paymentMethods.get(position), false);
                            viewModel.request.setWardId(viewModel.wards.get().get(position).getId());
                        }
                    });
                    viewBinding.actvPaymentMethod3.setAdapter(adapter);
                }
            }
        });

        viewModel.request.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                UpdateProfileRequest request = viewModel.request;

                switch (propertyId) {
                    case BR.fullName:
                        if (request.getFullName() == null || request.getFullName().isEmpty()) {
                            viewBinding.layoutFullname.setError("Họ và tên không được để trống");
                        } else {
                            viewBinding.layoutFullname.setError(null);
                        }
                        break;

                    case BR.password:
                        if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                            viewBinding.layoutPass.setError("Mật khẩu không được để trống");
                        } else {
                            viewBinding.layoutPass.setError(null);
                        }
                        break;
                    default:
                        viewModel.getProvinces(1,null);
                        viewModel.getDistricts(2, viewModel.request.getProvinceId());
                        viewModel.getWards(3, viewModel.request.getDistrictId());
                        break;
                }
            }
        });

        initView();

        viewModel.repository.getRoomService().userDao().loadAllToLiveData().observe(this, userEntities -> {
            Long userId = viewModel.repository.getSharedPreferences().getUserId();
            for (UserEntity userEntity : userEntities) {
                if (userEntity.getId().equals(userId)){
                    viewModel.request.setFullName(userEntity.getFullName());
                    viewModel.request.setPhone(userEntity.getPhone());
                    viewModel.request.setEmail(userEntity.getEmail());
                    viewModel.request.setProvinceId(userEntity.getProvinceId());
                    viewModel.request.setWardId(userEntity.getWardId());
                    viewModel.request.setDistrictId(userEntity.getDistrictId());
                    viewModel.request.setAddress(userEntity.getAddress());
                    viewModel.request.setAvatarPath(userEntity.getAvatar());
                }
            }
        });
    }

    public void updateProfile(){
        if (viewModel.request.getFullName() == null || viewModel.request.getFullName().isEmpty()) {
            viewBinding.layoutFullname.setError("Họ và tên không được để trống");
            return;
        } else {
            viewBinding.layoutFullname.setError(null);
        }
        if (viewModel.request.getOldPassword() == null || viewModel.request.getOldPassword().isEmpty()) {
            viewBinding.layoutPass.setError("Mật khẩu không được để trống");
            return;
        } else {
            viewBinding.layoutPass.setError(null);
        }
        viewModel.updateProfile();
    }

    private void initView() {
        viewModel.isShowPassword.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int pos = viewBinding.editPassword.getSelectionStart();
                if (!Objects.requireNonNull(viewModel.isShowPassword.get())) {
                    viewBinding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    viewBinding.editPassword.setTransformationMethod(null);
                }
                viewBinding.editPassword.setSelection(pos);

            }
        });
    }


    public void showDialogChooseImage() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image_picker);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity  = android.view.Gravity.CENTER;
        window.setAttributes(windowAttributes);

        RelativeLayout btnGallery = dialog.findViewById(R.id.btn_galery);
        RelativeLayout btnCamera = dialog.findViewById(R.id.btn_camera);
        btnGallery.setOnClickListener(v -> {
            // Gallery option clicked
            dialog.dismiss();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.STORAGE_REQUEST);

            } else {
                openGallery();
            }

        });
        btnCamera.setOnClickListener(v -> {
            // Camera option clicked
            dialog.dismiss();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_REQUEST);
            } else {
                openCamera();
            }
        });
        dialog.show();
    }

    private void openGallery() {
        ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .start();
    }

    private void openCamera() {
        ImagePicker.with(this)
                .cameraOnly()
                .cropSquare()
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
//                Glide.with(this)
//                        .load(selectedImageUri)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .into(viewBinding.imgAvatar);
                MultipartBody.Part imagePart = uriToMultipartBodyPart(selectedImageUri, "file");
                // Call API to upload image
                viewModel.doUploadAvatar(imagePart);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, getResources().getString(R.string.not_permission_gallery), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Constants.CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, getResources().getString(R.string.not_permission_camera), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private MultipartBody.Part uriToMultipartBodyPart(Uri uri, String partName) {
        try {
            File file = new File(RealPathUtil.getRealPath(this, uri));
            RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }
}
