package tpp.profixer.customer.ui.expert;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.databinding.ActivityExpertBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.course.CourseActivity;
import tpp.profixer.customer.ui.course.adapter.Course2Adapter;

public class ExpertActivity extends BaseActivity<ActivityExpertBinding, ExpertViewModel> {

    private Course2Adapter course2Adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_expert;
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

        viewModel.expertId = getIntent().getLongExtra("expert_id", -1);

        setLayoutRelatedCourses();

        viewModel.identification.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String rawHtml = viewModel.identification.get().getIntroduction();
                String cleanedHtml = rawHtml
                        .replaceAll("(?i)<p>(\\s|&nbsp;)*</p>", "")     // Xóa <p> chỉ có khoảng trắng hoặc &nbsp;
                        .replaceAll("(?i)<span>(\\s|&nbsp;)*</span>", ""); // Xóa <span> tương tự
                Spanned html = Html.fromHtml(cleanedHtml, Html.FROM_HTML_MODE_LEGACY);
                viewBinding.expandTextView.setText(html);
            }
        });

        viewModel.relatedCourses.observe(this, relatedCourses -> {
            course2Adapter.setData(relatedCourses);
        });

        viewModel.getExpertInfo();
    }

    private void setLayoutRelatedCourses(){
        course2Adapter = new Course2Adapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.rvCourses.setLayoutManager(layoutManager);
        viewBinding.rvCourses.setAdapter(course2Adapter);
        course2Adapter.setListener(new Course2Adapter.CourseListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent it = new Intent(ExpertActivity.this, CourseActivity.class);
                it.putExtra("course_id", course.getId());
//                it.putExtra("category_id", viewModel.categoryId);
                startActivity(it);
            }
        });
    }

}
