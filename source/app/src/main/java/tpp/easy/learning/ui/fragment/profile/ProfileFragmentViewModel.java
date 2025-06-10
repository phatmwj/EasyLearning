package tpp.easy.learning.ui.fragment.profile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.ObservableField;

import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.data.model.room.UserEntity;
import tpp.easy.learning.ui.base.fragment.BaseFragmentViewModel;
import tpp.easy.learning.ui.home.HomeActivity;

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
