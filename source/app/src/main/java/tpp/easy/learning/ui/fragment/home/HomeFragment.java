package tpp.easy.learning.ui.fragment.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import tpp.easy.learning.BR;
import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.request.Slide;
import tpp.easy.learning.data.model.api.response.Category;
import tpp.easy.learning.data.model.api.response.CategoryCourse;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.data.model.app.Image;
import tpp.easy.learning.data.model.room.UserEntity;
import tpp.easy.learning.databinding.FragmentHomeBinding;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.base.fragment.BaseFragment;
import tpp.easy.learning.ui.category.CategoryActivity;
import tpp.easy.learning.ui.course.CourseActivity;
import tpp.easy.learning.ui.fragment.home.adapter.CategoryAdapter;
import tpp.easy.learning.ui.fragment.home.adapter.ImageAdapter;

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

        viewModel.repository.getRoomService().userDao().loadAllToLiveData().observe(this, userEntities -> {
            Long userId = viewModel.repository.getSharedPreferences().getUserId();
            for (UserEntity userEntity : userEntities) {
                if (userEntity.getId().equals(userId)){
                    viewModel.profile.set(userEntity);
                }
            }
        });

        viewModel.slides.observe(this, slides -> {
            imageAdapter = new ImageAdapter(getContext(), slides);
            binding.viewPager2.setAdapter(imageAdapter);
            binding.circelIndicator3.setViewPager(binding.viewPager2);
            imageAdapter.setOnItemListener(new ImageAdapter.OnItemListener() {
                @Override
                public void onItemClick(Slide slide) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(slide.getUrl()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
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
                it.putExtra("category_id", course.getField().getId());
                startActivity(it);
            }
        });
    }


}
