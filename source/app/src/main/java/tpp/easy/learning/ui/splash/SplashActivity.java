package tpp.easy.learning.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tpp.easy.learning.BR;
import tpp.easy.learning.R;
import tpp.easy.learning.databinding.ActivityMainBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.ui.base.activity.BaseActivity;
import tpp.easy.learning.ui.home.HomeActivity;
import tpp.easy.learning.ui.map.MapActivity;


public class SplashActivity extends BaseActivity<ActivityMainBinding, SplashViewModel> {

    private String[] locationPermission = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {

                Boolean fineLocationGranted = null;
                Boolean coarseLocationGranted = null;

                fineLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                coarseLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_COARSE_LOCATION,false);

                if (fineLocationGranted || coarseLocationGranted) {
                    navigateToHome();
                } else {
                    // No location access granted.
                }
            });
    private static final Integer LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToHome();
            }
        },3000);
    }

    private void checkLocationPermission(){
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
//        for(String permission : locationPermission){
//            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, locationPermission, LOCATION_PERMISSION_REQUEST_CODE);
//                break;
//            };
//        };
    }

    private void navigateToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
