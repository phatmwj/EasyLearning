package tpp.easy.learning.ui.lesson.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.CustomLesson;
import tpp.easy.learning.data.model.api.response.Lesson;
import tpp.easy.learning.databinding.FragmentContentBinding;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.base.fragment.BaseFragment;
import tpp.easy.learning.ui.lesson.LessonActivity;
import tpp.easy.learning.ui.lesson.adapter.Lesson2Adapter;

public class ContentFragment extends BaseFragment<FragmentContentBinding, ContentViewModel> {
    private Long courseId;
    private Lesson2Adapter lesson2Adapter;
    private List<CustomLesson> customLessons = new ArrayList<>();
    public ContentFragment(Long courseId){
        this.courseId = courseId;
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    protected void performDataBinding() {

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayoutLesson();

        viewModel.course.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.course.get() == null) return;
                loadLessonData(viewModel.course.get().getLessons());
            }
        });

        viewModel.getCourseDetails(courseId);
    }

    private void setLayoutLesson(){
        lesson2Adapter = new Lesson2Adapter(getContext(), customLessons);
        binding.lvLesson.setAdapter(lesson2Adapter);

        binding.lvLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        binding.lvLesson.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                lesson2Adapter.setCurrentLesson(customLessons.get(groupPosition).getLessons().get(childPosition));
                lesson2Adapter.notifyDataSetChanged();
                LessonActivity lessonActivity = (LessonActivity) getActivity();
                lessonActivity.getLessonDetails(id);
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
        lesson2Adapter.notifyDataSetChanged();

        if(!customLessons.isEmpty()){
            if(!customLessons.get(0).getLessons().isEmpty()){
                lesson2Adapter.setCurrentLesson(customLessons.get(0).getLessons().get(0));
            }
            binding.lvLesson.expandGroup(0);
        }
    }
}
