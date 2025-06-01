package tpp.profixer.customer.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import lombok.Getter;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityHomeBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.dialog.ConfirmDialog;
import tpp.profixer.customer.ui.fragment.home.HomeFragment;
import tpp.profixer.customer.ui.fragment.income.IncomeFragment;
import tpp.profixer.customer.ui.fragment.notification.NotificationFragment;
import tpp.profixer.customer.ui.fragment.profile.ProfileFragment;
import tpp.profixer.customer.ui.fragment.study.StudyFragment;
import tpp.profixer.customer.utils.GPSObserver;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements NavigationBarView.OnItemSelectedListener{

    public Fragment activeFragment = new Fragment();
    @Getter
    private HomeFragment homeFragment;
    private IncomeFragment incomeFragment;
    private ProfileFragment profileFragment;
    private NotificationFragment notificationFragment;
    private StudyFragment studyFragment;
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

        viewModel.getCart();

        viewModel.textSearch.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                handleSearch(viewModel.textSearch.get());
            }
        });

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
            case R.id.study:
                replaceFragmentStudy();
                return true;
        }
        return false;
    }
    public void replaceFragmentStudy(){
        viewModel.title.set("Khóa học của bạn");
        viewModel.loginButton.set(true);
        viewModel.textSearch.set("");
        viewModel.isSearch.set(false);
        viewModel.notApp.set(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        studyFragment = (StudyFragment) fragmentManager.findFragmentByTag("study fragment");
        if(studyFragment == null){
            studyFragment = new StudyFragment();
            transaction.add(R.id.frameLayout, studyFragment, "study fragment");
        }
        else{
            transaction.show(studyFragment);
        }
        if (activeFragment != null && activeFragment != studyFragment) {
            transaction.hide(activeFragment);
        }
        transaction.commit();
        activeFragment = studyFragment;
    }
    public void replaceFragmentHome(){
        viewModel.title.set("");
        viewModel.loginButton.set(false);
        viewModel.textSearch.set("");
        viewModel.isSearch.set(false);
        viewModel.notApp.set(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("home fragment");
        if(homeFragment == null){
            homeFragment = new HomeFragment();
            transaction.add(R.id.frameLayout, homeFragment, "home fragment");
        }
        else{
            transaction.show(homeFragment);
        }
        if (activeFragment != null && activeFragment != homeFragment) {
            transaction.hide(activeFragment);
        }
        transaction.commit();
        activeFragment = homeFragment;
    }
    public void replaceFragmentProfile(){
        viewModel.title.set("Tài khoản");
        viewModel.loginButton.set(true);
        viewModel.textSearch.set("");
        viewModel.isSearch.set(false);
        viewModel.notApp.set(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        profileFragment = (ProfileFragment) fragmentManager.findFragmentByTag("profile fragment");
        if(profileFragment == null){
            profileFragment = new ProfileFragment();
            transaction.add(R.id.frameLayout, profileFragment, "profile fragment");
        }
        else{
            transaction.show(profileFragment);
        }
        if (activeFragment != null && activeFragment != profileFragment) {
            transaction.hide(activeFragment);
        }
        transaction.commit();
        activeFragment = profileFragment;
    }

    public void replaceFragmentIncome(){
        viewModel.title.set("");
        viewModel.loginButton.set(true);
//        viewModel.textSearch.set("");
        viewModel.isSearch.set(true);
        viewModel.notApp.set(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        incomeFragment = (IncomeFragment) fragmentManager.findFragmentByTag("income fragment");
        if(incomeFragment == null){
            incomeFragment = new IncomeFragment();
            transaction.add(R.id.frameLayout, incomeFragment, "income fragment");
        }
        else{
            transaction.show(incomeFragment);
        }
        if (activeFragment != null && activeFragment != incomeFragment) {
            transaction.hide(activeFragment);
        }
        transaction.commit();
        activeFragment = incomeFragment;
    }

    public void replaceFragmentNotification(){
        viewModel.title.set("Thông báo");
        viewModel.loginButton.set(true);
        viewModel.textSearch.set("");
        viewModel.isSearch.set(false);
        viewModel.notApp.set(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        notificationFragment = (NotificationFragment) fragmentManager.findFragmentByTag("notification fragment");
        if(notificationFragment == null){
            notificationFragment = new NotificationFragment();
            transaction.add(R.id.frameLayout, notificationFragment, "notification fragment");
        }
        else{
            transaction.show(notificationFragment);
        }
        if (activeFragment != null && activeFragment != notificationFragment) {
            transaction.hide(activeFragment);
        }
        transaction.commit();
        activeFragment = notificationFragment;
    }

    @Override
    public void onBackPressed() {
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.title.set("Bạn muốn thoát ứng dụng?");
        confirmDialog.titleRightButton.set("Thoát");
        confirmDialog.setListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void confirm() {
                confirmDialog.dismiss();
                finish();
            }
        });
        confirmDialog.show();
    }

    @Override
    public boolean showHeader() {
        return true;
    }

    public void handleSearch(String search){
        if(incomeFragment == null) return;
        incomeFragment.handleSearch(search);
    }
}
