package tpp.profixer.customer.ui.payment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.github.glailton.expandabletextview.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityPaymentBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;

public class PaymentActivity extends BaseActivity<ActivityPaymentBinding, PaymentViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
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

    @Override
    public boolean showHeader() {
        return true;
    }
}
