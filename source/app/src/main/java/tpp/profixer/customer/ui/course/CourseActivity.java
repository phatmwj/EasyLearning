package tpp.profixer.customer.ui.course;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.AmountReview;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.CustomLesson;
import tpp.profixer.customer.data.model.api.response.Lesson;
import tpp.profixer.customer.data.model.api.response.ReviewStar;
import tpp.profixer.customer.databinding.ActivityCourseBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.course.adapter.Course2Adapter;
import tpp.profixer.customer.ui.course.adapter.LessonAdapter;
import tpp.profixer.customer.ui.course.adapter.ReviewAdapter;
import tpp.profixer.customer.ui.course.adapter.ReviewStarAdapter;

public class CourseActivity extends BaseActivity<ActivityCourseBinding, CourseViewModel> {
    private LessonAdapter lessonAdapter;
    private List<CustomLesson> customLessons = new ArrayList<>();
    private ReviewStarAdapter reviewStarAdapter;
    private List<AmountReview> amountReviews = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private Course2Adapter course2Adapter;
    private Long categoryId;
    private Long courseId;

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

        setLayoutLessons();
        setLayoutReviewStar();
        setLayoutReviewList();
        setLayoutRelatedCourses();

        courseId = getIntent().getLongExtra("course_id", 0);
        categoryId = getIntent().getLongExtra("category_id", 0);

        viewBinding.oldMoney.setPaintFlags(viewBinding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewModel.course.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                loadLessonData(viewModel.course.get().getLessons());
            }
        });

        viewModel.reviewStar.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ReviewStar reviewStar = viewModel.reviewStar.get();
                if(reviewStar != null && reviewStar.getAmountReview() != null){
                    reviewStarAdapter.setTotalReview(reviewStar.getTotal());
                    amountReviews.clear();
                    amountReviews.addAll(reviewStar.getAmountReview());
                    amountReviews.sort(Comparator.comparingInt(AmountReview::getStar));
                    reviewStarAdapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.reviewList.observe(this, reviews -> {
            reviewAdapter.setData(reviews);
        });
        viewModel.relatedCourses.observe(this, relatedCourses -> {
            course2Adapter.setData(relatedCourses);
        });

        viewModel.getCourseDetails(courseId);
        viewModel.getReviewStar(courseId);
        viewModel.getReviewList(courseId);
        viewModel.getRelatedCourses(courseId, categoryId);
    }

    private void setLayoutLessons(){
        lessonAdapter = new LessonAdapter(this, customLessons);
        viewBinding.exlvLesson.setAdapter(lessonAdapter);
        viewBinding.exlvLesson.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        viewBinding.exlvLesson.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
    }

    private void loadLessonData(List<Lesson> lessons){
        customLessons.clear();
        if(lessons != null){
            lessons.sort(Comparator.comparingInt(Lesson::getOrdering));
            for(int i = 0; i < lessons.size(); i++){
                Lesson lesson = lessons.get(i);
                if(lesson.getKind() == 3){
                    CustomLesson customLesson = new CustomLesson();
                    customLesson.setLesson(lesson);
                    customLesson.setLessons(new ArrayList<>());
                    customLessons.add(customLesson);
                }else {
                    if(customLessons.isEmpty()) return;
                    CustomLesson customLesson = customLessons.get(customLessons.size() - 1);
                    customLesson.getLessons().add(lesson);
                }
            }
        }
        lessonAdapter.notifyDataSetChanged();

        if(!customLessons.isEmpty()){
            viewBinding.exlvLesson.expandGroup(0);
        }
    }

    private void setLayoutReviewStar(){
        reviewStarAdapter = new ReviewStarAdapter(this, amountReviews, 0);
        viewBinding.lvReviewStar.setAdapter(reviewStarAdapter);
    }

    private void setLayoutReviewList(){
        reviewAdapter = new ReviewAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.rvReview.setLayoutManager(layoutManager);
        viewBinding.rvReview.setAdapter(reviewAdapter);
    }

    private void setLayoutRelatedCourses(){
        course2Adapter = new Course2Adapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.rvRelatedCourse.setLayoutManager(layoutManager);
        viewBinding.rvRelatedCourse.setAdapter(course2Adapter);
        course2Adapter.setListener(new Course2Adapter.CourseListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent it = new Intent(CourseActivity.this, CourseActivity.class);
                it.putExtra("course_id", course.getId());
                it.putExtra("category_id", categoryId);
                startActivity(it);
            }
        });
    }

}
