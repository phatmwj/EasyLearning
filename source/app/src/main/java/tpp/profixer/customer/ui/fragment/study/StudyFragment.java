package tpp.profixer.customer.ui.fragment.study;

import android.content.Intent;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.databinding.FragmentStudyBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.di.component.FragmentComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;
import tpp.profixer.customer.ui.fragment.study.adpater.MyCourseAdapter;
import tpp.profixer.customer.ui.lesson.LessonActivity;

public class StudyFragment extends BaseFragment<FragmentStudyBinding, StudyFragmentViewModel> {
    private MyCourseAdapter myCourseAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    protected void performDataBinding() {
        setLayoutMyCourses();
        viewModel.courses.observe(this, courses -> {
            myCourseAdapter.setData(courses);
        });
        viewModel.getMyCourse();
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    private void setLayoutMyCourses(){
        myCourseAdapter = new MyCourseAdapter(getContext(), new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcMyCourse.setLayoutManager(linearLayoutManager);
        binding.rcMyCourse.setAdapter(myCourseAdapter);
        myCourseAdapter.setListener(new MyCourseAdapter.CourseListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent intent = new Intent(getContext(), LessonActivity.class);
                intent.putExtra("course_id", course.getId());
                startActivity(intent);
            }
        });
    }
    public void setIsFinished(boolean isFinished){
        viewModel.isFinished.set(isFinished);
        viewModel.getMyCourse();
    }

}
