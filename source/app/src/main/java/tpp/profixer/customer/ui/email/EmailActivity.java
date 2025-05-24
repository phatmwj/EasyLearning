package tpp.profixer.customer.ui.email;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.github.glailton.expandabletextview.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityEmailBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;

public class EmailActivity extends BaseActivity<ActivityEmailBinding, EmailViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_email;
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
    }

    public void requestForgetPassword(){

    }
}
