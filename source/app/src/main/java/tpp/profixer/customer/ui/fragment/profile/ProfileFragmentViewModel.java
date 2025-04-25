package tpp.profixer.customer.ui.fragment.profile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.ObservableField;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.room.UserEntity;
import tpp.profixer.customer.ui.base.fragment.BaseFragmentViewModel;
import tpp.profixer.customer.ui.home.HomeActivity;

public class ProfileFragmentViewModel extends BaseFragmentViewModel {
    public ObservableField<UserEntity> profile = new ObservableField<>();

    public  Repository repository;
    public ProfileFragmentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
        this.repository = repository;
//        profile.set(repository.getRoomService().userDao().findById(repository.getSharedPreferences().getUserId()).getValue());
    }



    public void logout(){
        repository.getSharedPreferences().removeKey("KEY_USER_ID");
        repository.getSharedPreferences().removeKey("KEY_BEARER_TOKEN");
//        repository.getRoomService().userDao().delete();
        ProFixerApplication.cartInfo = null;
        Intent it = application.getCurrentActivity().getIntent();
        application.getCurrentActivity().finish();
        application.getCurrentActivity().startActivity(it);
    }
}
