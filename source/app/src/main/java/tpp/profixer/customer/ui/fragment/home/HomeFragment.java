package tpp.profixer.customer.ui.fragment.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.request.Slide;
import tpp.profixer.customer.data.model.api.response.Category;
import tpp.profixer.customer.data.model.api.response.CategoryCourse;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.app.Image;
import tpp.profixer.customer.databinding.FragmentHomeBinding;
import tpp.profixer.customer.di.component.FragmentComponent;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;
import tpp.profixer.customer.ui.category.CategoryActivity;
import tpp.profixer.customer.ui.course.CourseActivity;
import tpp.profixer.customer.ui.fragment.home.adapter.CategoryAdapter;
import tpp.profixer.customer.ui.fragment.home.adapter.ImageAdapter;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>{
    private List<Slide> images = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private CategoryAdapter categoryAdapter;
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void performDataBinding() {
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        imageAdapter = new ImageAdapter(getContext(), images);
//        binding.viewPager2.setAdapter(imageAdapter);
//        binding.circelIndicator3.setViewPager(binding.viewPager2);

        getCategoryCourse();

        viewModel.slides.observe(this, slides -> {
            imageAdapter = new ImageAdapter(getContext(), slides);
            binding.viewPager2.setAdapter(imageAdapter);
            binding.circelIndicator3.setViewPager(binding.viewPager2);
        });

        viewModel.categoryCourses.observe(this, categoryCourses -> {
            categoryAdapter.addData(categoryCourses);
        });

        binding.swipeRefresh.setColorSchemeResources(R.color.app_color);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            categoryAdapter.setData(new ArrayList<>());
            viewModel.kind = 3;
            viewModel.getSlideShow();
            binding.swipeRefresh.setRefreshing(false);
        });

        viewModel.getSlideShow();
    }

    private void getCategoryCourse(){
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvCategoryCourse.setLayoutManager(linearLayoutManager);
        binding.rvCategoryCourse.setAdapter(categoryAdapter);
        categoryAdapter.setListener(new CategoryAdapter.CategoryListener() {
            @Override
            public void onItemClick(CategoryCourse categoryCourse) {
                Intent intent = new Intent(getContext(), CategoryActivity.class);
                intent.putExtra("category_id", categoryCourse.getCategory().getId());
                startActivity(intent);
            }

            @Override
            public void onCourseClick(Course course, Category category) {
                Intent it = new Intent(getContext(), CourseActivity.class);
                it.putExtra("course_id", course.getId());
                it.putExtra("category_id", category.getId());
                startActivity(it);
            }
        });
    }


}
