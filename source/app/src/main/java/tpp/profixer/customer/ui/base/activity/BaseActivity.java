package tpp.profixer.customer.ui.base.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import timber.log.Timber;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.R;
import tpp.profixer.customer.constant.Constants;
import tpp.profixer.customer.data.model.api.response.CartInfo;
import tpp.profixer.customer.data.socket.KittyRealtimeEvent;
import tpp.profixer.customer.data.socket.dto.Message;
import tpp.profixer.customer.databinding.LayoutHeaderTitleBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.di.component.DaggerActivityComponent;
import tpp.profixer.customer.di.module.ActivityModule;
import tpp.profixer.customer.ui.cart.CartActivity;
import tpp.profixer.customer.ui.dialog.ConfirmDialog;
import tpp.profixer.customer.ui.login.LoginActivity;
import tpp.profixer.customer.utils.DialogUtils;

import javax.inject.Inject;
import javax.inject.Named;

public abstract class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity implements KittyRealtimeEvent, LocationListener {

    protected B viewBinding;

    @Inject
    protected V viewModel;

    @Inject
    protected Context application;

    @Named("access_token")
    @Inject
    protected String token;
    protected Long userId;

    @Named("device_id")
    @Inject
    protected String deviceId;
    private Dialog progressDialog;
    // Listen all action from local
    private BroadcastReceiver globalApplicationReceiver;
    private IntentFilter filterGlobalApplication;
    private String[] locationPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //location
    LocationManager locationManager;
    protected Location location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection(getBuildComponent());
        super.onCreate(savedInstanceState);
        performDataBinding();
        updateCurrentActivity();

        viewModel.setToken(token);
        userId = viewModel.repository.getSharedPreferences().getUserId();
        viewModel.isLogin.set(token != null && !token.equals("NULL"));
        viewModel.setDeviceId(deviceId);

        viewModel.mIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (((ObservableBoolean) sender).get()) {
                    showProgressbar(getResources().getString(R.string.msg_loading));
                } else {
                    hideProgress();
                }
            }
        });
        viewModel.mErrorMessage.observe(this, toastMessage -> {
            if (toastMessage != null) {
                toastMessage.showMessage(getApplicationContext());
            }
        });
        viewModel.progressBarMsg.observe(this, progressBarMsg -> {
            if (progressBarMsg != null) {
                changeProgressBarMsg(progressBarMsg);
            }
        });

        filterGlobalApplication = new IntentFilter();
        filterGlobalApplication.addAction(Constants.ACTION_EXPIRED_TOKEN);
        globalApplicationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if (action.equals(Constants.ACTION_EXPIRED_TOKEN)) {
                    doExpireSession();
                }
            }
        };

        //
//        viewModel.isLogin.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                if(headerBinding != null){
//                    headerBinding.setIsLogin(viewModel.isLogin.get());
//                    headerBinding.executePendingBindings();
//                }
//            }
//        });
        setLayoutHeader();
        if(ProFixerApplication.cartInfo != null){
            viewModel.cartInfo.setValue(ProFixerApplication.cartInfo);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000L, 0L,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(globalApplicationReceiver, filterGlobalApplication);
        updateCurrentActivity();
        if(ProFixerApplication.cartInfo != null){
            viewModel.cartInfo.setValue(ProFixerApplication.cartInfo);
        }
        userId = viewModel.repository.getSharedPreferences().getUserId();
        token = viewModel.repository.getSharedPreferences().getToken();
        viewModel.isLogin.set(token != null && !token.equals("NULL"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(globalApplicationReceiver);
    }

    public abstract @LayoutRes int getLayoutId();

    public abstract int getBindingVariable();

    public void doExpireSession() {
        //implement later

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        return false;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void performDataBinding() {
        viewBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewBinding.setVariable(getBindingVariable(), viewModel);
        viewBinding.setVariable(BR.a, this);
        viewBinding.executePendingBindings();
    }

    public void showProgressbar(String msg) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = DialogUtils.createDialogLoading(this, msg);
        progressDialog.show();
    }

    public void changeProgressBarMsg(String msg) {
        if (progressDialog != null) {
            ((TextView) progressDialog.findViewById(R.id.progressbar_msg)).setText(msg);
        }
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private ActivityComponent getBuildComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(((ProFixerApplication) getApplication()).getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public abstract void performDependencyInjection(ActivityComponent buildComponent);

    private void updateCurrentActivity() {
        ProFixerApplication mvvmApplication = (ProFixerApplication) application;
        mvvmApplication.setCurrentActivity(this);
    }

    //socket listener

    @Override
    public void onConnectionClosed() {

    }

    @Override
    public void onConnectionClosing() {

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onConnectionOpened() {

    }

    @Override
    public void onMessageReceived(Message message) {

    }

    //locationListener
    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Timber.tag("Location").d("Location: %s", location);
        this.location = location;
    }

    //
    LayoutHeaderTitleBinding headerBinding;
    private void setLayoutHeader(){
        if(showHeader()){
            headerBinding = DataBindingUtil.getBinding(viewBinding.getRoot().findViewById(R.id.ui_header));
            if(headerBinding != null){
                ProFixerApplication mvvmApplication = (ProFixerApplication) application;
                viewModel.cartInfo.observe(mvvmApplication.getCurrentActivity(), cart -> {
                    handleCart(cart);
                    headerBinding.setCountItemCart(cart.getContent().getCartItems() != null ? cart.getContent().getCartItems().size() : 0);

                });
                headerBinding.login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
                headerBinding.layoutCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                        startActivity(intent);
                    }
                });
                headerBinding.executePendingBindings();
            }
        }
    }

    public boolean showHeader(){
        return false;
    }

    public void handleCart(CartInfo cartInfo){

    }

    //
    public void confirmLogin(){
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.title.set("Vui lòng đăng nhập để tiếp tục");
        confirmDialog.titleRightButton.set("Đăng nhập");
        confirmDialog.setListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void confirm() {
                confirmDialog.dismiss();
                navigateToLogin();
            }
        });
        confirmDialog.show();
    }

    public void navigateToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
