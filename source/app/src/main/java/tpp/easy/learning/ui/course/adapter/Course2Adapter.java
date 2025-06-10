package tpp.easy.learning.ui.course.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Setter;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.databinding.ItemCourse2Binding;

public class Course2Adapter extends RecyclerView.Adapter<Course2Adapter.Course2ViewHolder> {
    private Context context;
    private List<Course> data;

    private LayoutInflater layoutInflater;
    @Setter
    private CourseListener listener;

    public Course2Adapter(Context context, List<Course> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Course> relatedCourses) {
        this.data = relatedCourses;
        notifyDataSetChanged();
    }

    public void addData(List<Course> relatedCourses) {
        this.data.addAll(relatedCourses);
        notifyDataSetChanged();
    }

    public interface CourseListener{
        void onCourseClick(Course course);
    }

    @NonNull
    @Override
    public Course2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCourse2Binding binding = ItemCourse2Binding.inflate(layoutInflater, parent, false);
        return new Course2ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Course2ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class Course2ViewHolder extends RecyclerView.ViewHolder {
        private ItemCourse2Binding binding;
        private int position;
        public Course2ViewHolder(@NonNull ItemCourse2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if(listener != null){
                    listener.onCourseClick(data.get(position));
                }
            });
        }

        public void onBind(int position){
            this.position = position;
            binding.setItem(data.get(position));
            binding.oldMoney.setPaintFlags(binding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.executePendingBindings();
        }
    }
}
