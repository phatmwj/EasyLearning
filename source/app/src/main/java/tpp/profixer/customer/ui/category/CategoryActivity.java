package tpp.profixer.customer.ui.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.Category;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.databinding.ActivityCategoryBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.course.CourseActivity;
import tpp.profixer.customer.ui.course.adapter.Course2Adapter;

public class CategoryActivity extends BaseActivity<ActivityCategoryBinding, CategoryViewModel> {
    Course2Adapter course2Adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_category;
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

        viewModel.categoryId = getIntent().getLongExtra("category_id", -1L);
        if(ProFixerApplication.categories != null && !ProFixerApplication.categories.isEmpty()){
            for(Category category : ProFixerApplication.categories){
                if(category.getId().equals(viewModel.categoryId)){
                    viewModel.title.set(category.getName());
                    break;
                }
            }
        }

        setLayoutRelatedCourses();
        viewModel.courses.observe(this, courses ->{
            if(viewModel.page.get() == 0){
                course2Adapter.setData(courses);
                return;
            }
            course2Adapter.addData(courses);
        });

        viewBinding.swipeRefresh.setColorSchemeResources(R.color.app_color);
        viewBinding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.page.set(0);
            viewModel.getCoursesByCategory();
            viewBinding.swipeRefresh.setRefreshing(false);
        });
        viewModel.getCoursesByCategory();
    }

    private void setLayoutRelatedCourses(){
        course2Adapter = new Course2Adapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.rvCourse.setLayoutManager(layoutManager);
        viewBinding.rvCourse.setAdapter(course2Adapter);
        course2Adapter.setListener(new Course2Adapter.CourseListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent it = new Intent(CategoryActivity.this, CourseActivity.class);
                it.putExtra("course_id", course.getId());
                it.putExtra("category_id", viewModel.categoryId);
                startActivity(it);
            }
        });
    }

    public void loadMore(){
        viewModel.page.set(viewModel.page.get() + 1);
        viewModel.getCoursesByCategory();
    }
}
