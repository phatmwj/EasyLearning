package tpp.profixer.customer.ui.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tpp.profixer.customer.data.model.api.response.CategoryCourse;
import tpp.profixer.customer.databinding.ItemCagoryCoursesBinding;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<CategoryCourse> data;

    private LayoutInflater layoutInflater;

    public CategoryAdapter(Context context, List<CategoryCourse> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCagoryCoursesBinding binding = ItemCagoryCoursesBinding.inflate(layoutInflater, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setData(List<CategoryCourse> categoryCourses) {
        this.data = categoryCourses;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ItemCagoryCoursesBinding binding;
        private int position;
        public CategoryViewHolder(@NonNull ItemCagoryCoursesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position){
            this.position = position;
            binding.setItem(data.get(position));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            CourseAdapter adapter = new CourseAdapter(context, data.get(position).getCourses());
            binding.rvCourse.setLayoutManager(layoutManager);
            binding.rvCourse.setAdapter(adapter);
            binding.executePendingBindings();
        }
    }
}
