package tpp.easy.learning.ui.lesson.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.easy.learning.R;
import tpp.easy.learning.databinding.FragmentReviewBinding;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.base.fragment.BaseFragment;
import tpp.easy.learning.ui.course.adapter.ReviewAdapter;
import tpp.easy.learning.ui.dialog.ReviewDialog;

public class ReviewFragment extends BaseFragment<FragmentReviewBinding, ReviewViewModel> {
    private Long courseId;
    private Long expertId;
    private ReviewAdapter reviewAdapter;
    public ReviewFragment(Long courseId, Long expertId){
        this.courseId = courseId;
        this.expertId = expertId;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.courseId = courseId;
        viewModel.expertId = expertId;
        setLayoutReviewList();
        viewModel.reviewList.observe(this, reviews -> {
            reviewAdapter.setData(reviews);
        });
        viewModel.getReviewList(courseId);
    }

    private void setLayoutReviewList(){
        reviewAdapter = new ReviewAdapter(getContext(), new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvReview.setLayoutManager(layoutManager);
        binding.rvReview.setAdapter(reviewAdapter);
    }

    public void dialogReview(){
        ReviewDialog reviewDialog = new ReviewDialog(getContext());
        reviewDialog.show();
    }
}
