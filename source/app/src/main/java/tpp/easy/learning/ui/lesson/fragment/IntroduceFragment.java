package tpp.easy.learning.ui.lesson.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import lombok.Setter;
import tpp.easy.learning.R;
import tpp.easy.learning.databinding.FragmentIntroduceBinding;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.base.fragment.BaseFragment;

public class IntroduceFragment extends BaseFragment<FragmentIntroduceBinding, IntroduceViewModel> {
    private Long courseId;
    public IntroduceFragment(Long courseId){
        this.courseId = courseId;
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_introduce;
    }

    @Override
    protected void performDataBinding() {
        binding.executePendingBindings();
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getCourseDetails(courseId);
    }
}
