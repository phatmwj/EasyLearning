package tpp.profixer.customer.ui.account;

import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.room.UserEntity;
import tpp.profixer.customer.databinding.ActivityAccountBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;

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
}
