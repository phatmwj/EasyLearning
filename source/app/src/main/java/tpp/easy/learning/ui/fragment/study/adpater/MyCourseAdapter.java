package tpp.easy.learning.ui.fragment.study.adpater;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Setter;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.databinding.ItemMycourseBinding;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.MyCourseViewHolder> {
    private Context context;
    private List<Course> data;

    private LayoutInflater layoutInflater;
    @Setter
    private CourseListener listener;

    public MyCourseAdapter(Context context, List<Course> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Course> relatedCourses) {
        this.data = relatedCourses;
        notifyDataSetChanged();
    }

    public interface CourseListener{
        void onCourseClick(Course course);
    }

    @NonNull
    @Override
    public MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMycourseBinding binding = ItemMycourseBinding.inflate(layoutInflater, parent, false);
        return new MyCourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class MyCourseViewHolder extends RecyclerView.ViewHolder {
        private ItemMycourseBinding binding;
        private int position;
        public MyCourseViewHolder(@NonNull ItemMycourseBinding binding) {
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
