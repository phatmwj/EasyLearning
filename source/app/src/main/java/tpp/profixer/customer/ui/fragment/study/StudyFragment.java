package tpp.profixer.customer.ui.fragment.study;

import androidx.databinding.library.baseAdapters.BR;

import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.FragmentStudyBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.di.component.FragmentComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;

public class StudyFragment extends BaseFragment<FragmentStudyBinding, StudyFragmentViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    protected void performDataBinding() {

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

}
