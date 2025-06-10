package tpp.easy.learning.ui.fragment.income;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.easy.learning.BR;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.databinding.FragmentIncomeBinding;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.base.fragment.BaseFragment;
import tpp.easy.learning.ui.category.CategoryActivity;
import tpp.easy.learning.ui.course.CourseActivity;
import tpp.easy.learning.ui.course.adapter.Course2Adapter;
import tpp.easy.learning.ui.fragment.income.adapter.CategoryAdapter;

public class IncomeFragment extends BaseFragment<FragmentIncomeBinding, IncomeFragmentViewModel> {
    private CategoryAdapter categoryAdapter;
    Course2Adapter course2Adapter;
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_income;
    }

    @Override
    protected void performDataBinding() {
        setLayoutCategory();
        setLayoutCourses();

        viewModel.courses.observe(this, courses -> {
            course2Adapter.setData(courses);
            viewModel.totalElements.set(courses != null ? courses.size() : 0);
        });
        viewModel.query.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                viewModel.searchCourses();
            }
        });
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

    private void setLayoutCategory(){
        categoryAdapter = new CategoryAdapter(getContext(), ProFixerApplication.categories);
        binding.lvCategory.setAdapter(categoryAdapter);
        binding.lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CategoryActivity.class);
                intent.putExtra("category_id", id);
                startActivity(intent);
            }
        });
    }

    public void handleSearch(String search) {
        viewModel.query.set(search);
    }

    private void setLayoutCourses(){
        course2Adapter = new Course2Adapter(getContext(), new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvCourse.setLayoutManager(layoutManager);
        binding.rvCourse.setAdapter(course2Adapter);
        course2Adapter.setListener(new Course2Adapter.CourseListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent it = new Intent(getActivity(), CourseActivity.class);
                it.putExtra("course_id", course.getId());
                it.putExtra("category_id", viewModel.categoryId);
                startActivity(it);
            }
        });
    }
}
