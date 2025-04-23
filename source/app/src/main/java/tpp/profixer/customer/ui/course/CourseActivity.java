package tpp.profixer.customer.ui.course;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.CustomLesson;
import tpp.profixer.customer.data.model.api.response.Lesson;
import tpp.profixer.customer.databinding.ActivityCourseBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.course.adapter.LessonAdapter;

public class CourseActivity extends BaseActivity<ActivityCourseBinding, CourseViewModel> {
    private LessonAdapter lessonAdapter;
    private List<CustomLesson> customLessons = new ArrayList<>();
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

        Long courseId = getIntent().getLongExtra("course_id", 0);

        viewBinding.oldMoney.setPaintFlags(viewBinding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewModel.course.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                loadLessonData(viewModel.course.get().getLessons());
            }
        });

        viewModel.getCourseDetails(courseId);

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
}
