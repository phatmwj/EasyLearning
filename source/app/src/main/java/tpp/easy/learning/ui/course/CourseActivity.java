package tpp.easy.learning.ui.course;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.AmountReview;
import tpp.easy.learning.data.model.api.response.CartInfo;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.data.model.api.response.CustomLesson;
import tpp.easy.learning.data.model.api.response.Lesson;
import tpp.easy.learning.data.model.api.response.ReviewStar;
import tpp.easy.learning.databinding.ActivityCourseBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.ui.base.BaseCallback;
import tpp.easy.learning.ui.base.activity.BaseActivity;
import tpp.easy.learning.ui.cart.CartActivity;
import tpp.easy.learning.ui.course.adapter.Course2Adapter;
import tpp.easy.learning.ui.course.adapter.LessonAdapter;
import tpp.easy.learning.ui.course.adapter.ReviewAdapter;
import tpp.easy.learning.ui.course.adapter.ReviewStarAdapter;
import tpp.easy.learning.ui.dialog.CourseDialog;
import tpp.easy.learning.ui.lesson.LessonActivity;
import tpp.easy.learning.ui.payment.PaymentActivity;

public class CourseActivity extends BaseActivity<ActivityCourseBinding, CourseViewModel> {
    private LessonAdapter lessonAdapter;
    private List<CustomLesson> customLessons = new ArrayList<>();
    private ReviewStarAdapter reviewStarAdapter;
    private List<AmountReview> amountReviews = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private Course2Adapter course2Adapter;

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

        viewModel.courseId = getIntent().getLongExtra("course_id", 0);
        viewModel.categoryId = getIntent().getLongExtra("category_id", 0);

        viewBinding.oldMoney.setPaintFlags(viewBinding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewModel.course.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                loadLessonData(viewModel.course.get().getLessons());
                if(viewModel.course.get().getIsBuy()){
                    viewModel.courseState.set(2);
                }else if(viewModel.course.get().getIsProcessing()){
                    viewModel.courseState.set(3);
                }
                String rawHtml = viewModel.course.get().getDescription();
                String cleanedHtml = rawHtml
                        .replaceAll("(?i)<p>(\\s|&nbsp;)*</p>", "")     // Xóa <p> chỉ có khoảng trắng hoặc &nbsp;
                        .replaceAll("(?i)<span>(\\s|&nbsp;)*</span>", ""); // Xóa <span> tương tự
                Spanned html = Html.fromHtml(cleanedHtml, Html.FROM_HTML_MODE_LEGACY);
                viewBinding.expandTextView.setText(html);
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

        viewModel.getCourseDetails(viewModel.courseId);
        viewModel.getReviewStar(viewModel.courseId);
        viewModel.getReviewList(viewModel.courseId);
        viewModel.getRelatedCourses(viewModel.courseId, viewModel.categoryId);
    }

    @Override
    public boolean showHeader() {
        return true;
    }

    @Override
    public void handleCart(CartInfo cartInfo) {
        super.handleCart(cartInfo);
        if(viewModel.course.get() != null &&viewModel.course.get().getIsBuy()){
            viewModel.courseState.set(2);
            return;
        }
        if(viewModel.course.get() != null &&viewModel.course.get().getIsProcessing()){
            viewModel.courseState.set(3);
            return;
        }
        boolean isFind = false;
        if(cartInfo.getTotalElements() != null && cartInfo.getTotalElements() != 0){
            for(int i = 0; i < cartInfo.getContent().getCartItems().size(); i++){
                if(cartInfo.getContent().getCartItems().get(i).getCourse().getId().equals(viewModel.courseId)){
                    viewModel.courseState.set(1);
                    isFind = true;
                    break;
                }
            }
        }
        if(!isFind){
            viewModel.courseState.set(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setLayoutLessons(){
        lessonAdapter = new LessonAdapter(this, customLessons);
        viewBinding.exlvLesson.setAdapter(lessonAdapter);
        viewBinding.exlvLesson.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Lesson lesson = customLessons.get(groupPosition).getLessons().get(childPosition);
                if(lesson != null && lesson.getKind() == 2 && lesson.getIsPreview() != null && lesson.getIsPreview()){
                    viewModel.getLessonDetails(lesson.getId(), new BaseCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailed() {

                        }

                        @Override
                        public void onError(Exception exception) {

                        }
                    });
                }
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
                it.putExtra("category_id", course.getField().getId());
                startActivity(it);
            }
        });
    }

    public void handleButtonCart(){
        if(token == null || token.isEmpty() || token.equals("NULL")){
            confirmLogin();
            return;
        }
        switch (viewModel.courseState.get()){
            case 0:
                viewModel.addToCart(new BaseCallback() {
                    @Override
                    public void onSuccess() {
                        handleAddToCartSuccess();
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onError(Exception exception) {

                    }
                });
                break;
            case 1:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case 2:
                Intent it = new Intent(this, LessonActivity.class);
                it.putExtra("course_id", viewModel.courseId);
                if(viewModel.course.get().getExpert() != null && viewModel.course.get().getExpert().getId() != null){
                    it.putExtra("expert_id", viewModel.course.get().getExpert().getId());
                }
                startActivity(it);
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    public void handleAddToCartSuccess(){
        CourseDialog courseDialog = new CourseDialog(this);
        courseDialog.course.set(viewModel.course.get());
        courseDialog.show();
    }

    public void buyCourse(){
        if(token == null || token.isEmpty() || token.equals("NULL")){
            confirmLogin();
            return;
        }
        if(viewModel.courseState.get() == 0){
            viewModel.addToCart(new BaseCallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(CourseActivity.this, PaymentActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onError(Exception exception) {

                }
            });
        }else if(viewModel.courseState.get() == 1){
            Intent intent = new Intent(CourseActivity.this, PaymentActivity.class);
            startActivity(intent);
        }

    }



}
