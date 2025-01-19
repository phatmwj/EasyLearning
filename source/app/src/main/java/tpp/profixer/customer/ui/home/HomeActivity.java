package tpp.profixer.customer.ui.home;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import lombok.Getter;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityHomeBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.fragment.home.HomeFragment;
import tpp.profixer.customer.ui.fragment.income.IncomeFragment;
import tpp.profixer.customer.ui.fragment.notification.NotificationFragment;
import tpp.profixer.customer.ui.fragment.profile.ProfileFragment;
import tpp.profixer.customer.utils.GPSObserver;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements NavigationBarView.OnItemSelectedListener{

    public Fragment activeFragment = new Fragment();
    @Getter
    private HomeFragment homeFragment;
    private IncomeFragment incomeFragment;
    private ProfileFragment profileFragment;
    private NotificationFragment notificationFragment;
    private GPSObserver gpsObserver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
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

        viewBinding.navigationView.setOnItemSelectedListener(this);

        viewBinding.navigationView.setSelectedItemId(R.id.home);
        Handler handler = new Handler();
        gpsObserver = new GPSObserver(new Handler(), this);
        gpsObserver.onChange(gpsObserver.isGPSEnabled(this));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gpsObserver != null) {
            getContentResolver().unregisterContentObserver(gpsObserver);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (gpsObserver != null) {
            getContentResolver().registerContentObserver(
                    Settings.Secure.getUriFor(Settings.Secure.LOCATION_MODE),
                    false,
                    gpsObserver
            );
            gpsObserver.onChange(gpsObserver.isGPSEnabled(this));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                replaceFragmentHome();
                return true;
            case R.id.account:
                replaceFragmentProfile();
                return true;
            case R.id.income:
                replaceFragmentIncome();
                return true;
            case R.id.notification:
                replaceFragmentNotification();
                return true;
        }
        return false;
    }

    public void replaceFragmentHome(){
        if(homeFragment == null){
            homeFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, homeFragment, "home fragment").hide(activeFragment).commit();
        }
        else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment).show(homeFragment).commit();
        }
        activeFragment = homeFragment;
    }
    public void replaceFragmentProfile(){
        if(profileFragment == null){
            profileFragment = new ProfileFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, profileFragment, "profile fragment").hide(activeFragment).commit();
        }
        else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment).show(profileFragment).commit();
        }
        activeFragment = profileFragment;
    }

    public void replaceFragmentIncome(){
        if(incomeFragment == null){
            incomeFragment = new IncomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, incomeFragment, "income fragment").hide(activeFragment).commit();
        }
        else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment).show(incomeFragment).commit();
        }
        activeFragment = incomeFragment;
    }

    public void replaceFragmentNotification(){
        if(notificationFragment == null){
            notificationFragment = new NotificationFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, notificationFragment, "notification fragment").hide(activeFragment).commit();
        }
        else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment).show(notificationFragment).commit();
        }
        activeFragment = notificationFragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
