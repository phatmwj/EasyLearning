package tpp.easy.learning.ui.email;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import io.github.glailton.expandabletextview.BR;
import tpp.easy.learning.R;
import tpp.easy.learning.databinding.ActivityEmailBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.ui.base.activity.BaseActivity;

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
        viewModel.email.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.email.get() == null || viewModel.email.get().isEmpty()){
                    viewBinding.layoutEmail.setError("Email không được để trống");
                }else {
                    viewBinding.layoutEmail.setError(null);
                }
            }
        });
    }

    public void requestForgetPassword(){
        if(viewModel.email.get() == null || viewModel.email.get().isEmpty()){
            viewBinding.layoutEmail.setError("Email không được để trống");
        }
        viewModel.requestForgetPassword();
    }
}
