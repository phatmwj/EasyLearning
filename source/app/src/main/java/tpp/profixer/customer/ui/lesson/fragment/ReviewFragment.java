package tpp.profixer.customer.ui.lesson.fragment;

import androidx.databinding.library.baseAdapters.BR;

import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.FragmentReviewBinding;
import tpp.profixer.customer.di.component.FragmentComponent;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;

public class ReviewFragment extends BaseFragment<FragmentReviewBinding, ReviewViewModel> {
    private Long courseId;
    public ReviewFragment(Long courseId){
        this.courseId = courseId;
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_review;
    }

    @Override
    protected void performDataBinding() {

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}
