package tpp.easy.learning.ui.payment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.github.glailton.expandabletextview.BR;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.CartItem;
import tpp.easy.learning.databinding.ActivityPaymentBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.ui.base.activity.BaseActivity;
import tpp.easy.learning.ui.payment.adapter.PaymentAdapter;
import tpp.easy.learning.ui.qrcode.QrcodeActivity;

public class PaymentActivity extends BaseActivity<ActivityPaymentBinding, PaymentViewModel> {
    PaymentAdapter paymentAdapter;
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
        setLayoutCart();
        viewModel.cartInfo.observe(this, cartInfo -> {
            if (cartInfo != null) {
                viewModel.cartInfoF.set(cartInfo);
                paymentAdapter.setData(cartInfo.getContent().getCartItems());
                if(cartInfo.getContent().getCartItems() != null){
                    int price = 0;
                    int oldPrice = 0;
                    for(CartItem cartItem : cartInfo.getContent().getCartItems()){
                        price = price + cartItem.getCourse().getMoney();
                        oldPrice = oldPrice + cartItem.getCourse().getPrice();
                    }
                    viewModel.price.set(price);
                    viewModel.oldPrice.set(oldPrice);
                }
            }
        });
        viewModel.getCart();
    }

    @Override
    public boolean showHeader() {
        return true;
    }

    private void setLayoutCart(){
        paymentAdapter = new PaymentAdapter(this, new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.rvCart.setLayoutManager(linearLayoutManager);
        viewBinding.rvCart.setAdapter(paymentAdapter);
        paymentAdapter.setListener(new PaymentAdapter.PaymentListener() {

        });
    }

    public void navigateToQrcode(){
        Intent intent = new Intent(this, QrcodeActivity.class);
        startActivity(intent);
    }
}
