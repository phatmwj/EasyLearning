package tpp.profixer.customer.ui.course;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityCourseBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;

public class CourseActivity extends BaseActivity<ActivityCourseBinding, CourseViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_course;
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

        Long courseId = getIntent().getLongExtra("course_id", 0);
        viewModel.getCourseDetails(courseId);

        viewBinding.oldMoney.setPaintFlags(viewBinding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }
}
