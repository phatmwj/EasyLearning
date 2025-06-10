package tpp.easy.learning.ui.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import tpp.easy.learning.BR;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.app.AccountBtn;
import tpp.easy.learning.data.model.room.UserEntity;
import tpp.easy.learning.databinding.FragmentProfileBinding;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.account.AccountActivity;
import tpp.easy.learning.ui.base.fragment.BaseFragment;
import tpp.easy.learning.ui.changepassword.ChangePasswordActivity;
import tpp.easy.learning.ui.dialog.ConfirmDialog;
import tpp.easy.learning.ui.fragment.profile.adapter.AccountAdapter;
import tpp.easy.learning.ui.login.LoginActivity;

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
//        data.add(new AccountBtn(2,"Ví của tôi", R.drawable.ic_wallet));
        data.add(new AccountBtn(3,"Đổi mật khẩu", R.drawable.ic_password));

        accountAdapter = new AccountAdapter(getContext(), data);
        binding.lvAccount.setAdapter(accountAdapter);
        binding.lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id){
                    case 1:
                        Intent it = new Intent(getContext(), AccountActivity.class);
                        startActivity(it);
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
        data.add(new AccountBtn(1,"Giới thiệu về Easy Learning", R.drawable.ic_info));
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

    public void confirmLogout(){
        ConfirmDialog confirmDialog = new ConfirmDialog(getContext());
        confirmDialog.title.set("Bạn muốn đăng xuất?");
        confirmDialog.titleRightButton.set("Đăng xuất");
        confirmDialog.setListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void confirm() {
                confirmDialog.dismiss();
                viewModel.logout();
            }
        });
        confirmDialog.show();
    }

}
