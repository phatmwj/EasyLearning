package tpp.easy.learning.ui.base.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;

import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.R;
import tpp.easy.learning.di.component.DaggerFragmentComponent;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.di.module.FragmentModule;
import tpp.easy.learning.utils.DialogUtils;

import javax.inject.Inject;
import javax.inject.Named;


public abstract class BaseFragment<B extends ViewDataBinding, V extends BaseFragmentViewModel> extends Fragment{

    protected B binding;
    @Inject
    protected V viewModel;
    @Named("access_token")
    @Inject
    protected String token;
    private Dialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        performDependencyInjection(getBuildComponent());
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        binding.setVariable(getBindingVariable(), viewModel);
        binding.setVariable(BR.f, this);
        binding.setLifecycleOwner(this);
        performDataBinding();
        viewModel.setToken(token);
        viewModel.mErrorMessage.observe(getViewLifecycleOwner(), toastMessage -> {
            if (toastMessage != null) {
                toastMessage.showMessage(requireContext());
            }
        });
        viewModel.mIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (((ObservableBoolean) sender).get()) {
                    showProgressbar(getResources().getString(R.string.msg_loading));
                } else {
                    hideProgress();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public abstract int getBindingVariable();

    protected abstract int getLayoutId();

    protected abstract void performDataBinding();

    protected abstract void performDependencyInjection(FragmentComponent buildComponent);

    private FragmentComponent getBuildComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(((ProFixerApplication) requireActivity().getApplication()).getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    public void showProgressbar(String msg) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = DialogUtils.createDialogLoading(requireContext(), msg);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
