package tpp.profixer.customer.ui.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.app.AccountBtn;
import tpp.profixer.customer.data.model.room.UserEntity;
import tpp.profixer.customer.databinding.FragmentProfileBinding;
import tpp.profixer.customer.di.component.FragmentComponent;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;
import tpp.profixer.customer.ui.changepassword.ChangePasswordActivity;
import tpp.profixer.customer.ui.fragment.profile.adapter.AccountAdapter;
import tpp.profixer.customer.ui.login.LoginActivity;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileFragmentViewModel> {
    private AccountAdapter accountAdapter;
    private AccountAdapter aboutAdapter;
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void performDataBinding() {
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLayoutAccount();
        setLayoutAbout();

        viewModel.repository.getRoomService().userDao().loadAllToLiveData().observe(this, userEntities -> {
            Long userId = viewModel.repository.getSharedPreferences().getUserId();
            for (UserEntity userEntity : userEntities) {
                if (userEntity.getId().equals(userId)){
                    viewModel.profile.set(userEntity);
                }
            }
        });
    }

    private void setLayoutAccount(){
        List<AccountBtn> data = new ArrayList<>();
        data.add(new AccountBtn(1,"Hồ sơ", R.drawable.ic_icon_account));
        data.add(new AccountBtn(2,"Ví của tôi", R.drawable.ic_wallet));
        data.add(new AccountBtn(3,"Đổi mật khẩu", R.drawable.ic_password));

        accountAdapter = new AccountAdapter(getContext(), data);
        binding.lvAccount.setAdapter(accountAdapter);
        binding.lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setLayoutAbout(){
        List<AccountBtn> data = new ArrayList<>();
        data.add(new AccountBtn(1,"Giới thiệu về LifeUni", R.drawable.ic_info));
        data.add(new AccountBtn(2,"Thông tin liên hệ", R.drawable.ic_about));

        aboutAdapter = new AccountAdapter(getContext(), data);
        binding.lvAbout.setAdapter(aboutAdapter);
        binding.lvAbout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void navigateToLogin(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

}
