package tpp.profixer.customer.ui.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.databinding.ItemCourseBinding;
import tpp.profixer.customer.utils.ScreenUtils;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private Context context;
    private List<Course> data;

    private LayoutInflater layoutInflater;

    public CourseAdapter(Context context, List<Course> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCourseBinding binding = ItemCourseBinding.inflate(layoutInflater, parent, false);
        ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(context)/5*2;
        binding.getRoot().setLayoutParams(layoutParams);
        return new CourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private ItemCourseBinding binding;
        private int position;
        public CourseViewHolder(@NonNull ItemCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position){
            this.position = position;
            binding.setItem(data.get(position));
            binding.executePendingBindings();
        }
    }
}
