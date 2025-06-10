package tpp.easy.learning.ui.fragment.study;

import android.content.Intent;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.databinding.FragmentStudyBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.base.activity.BaseActivity;
import tpp.easy.learning.ui.base.fragment.BaseFragment;
import tpp.easy.learning.ui.fragment.study.adpater.MyCourseAdapter;
import tpp.easy.learning.ui.lesson.LessonActivity;

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
                if(course.getExpert() != null && course.getExpert().getId() != null){
                    intent.putExtra("expert_id", course.getExpert().getId());
                }
                startActivity(intent);
            }
        });
    }
    public void setIsFinished(boolean isFinished){
        viewModel.isFinished.set(isFinished);
        viewModel.getMyCourse();
    }

}
