package tpp.easy.learning.ui.fragment.income.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import tpp.easy.learning.data.model.api.response.Category;
import tpp.easy.learning.databinding.ItemCategoryBinding;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> data;
    LayoutInflater layoutInflater;

    public CategoryAdapter(Context context, List<Category> data){
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId().longValue();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemCategoryBinding binding;
        if(convertView == null){
            binding = ItemCategoryBinding.inflate(layoutInflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (ItemCategoryBinding) convertView.getTag();
        }
        binding.setItem(data.get(position));
        binding.executePendingBindings();
        return convertView;
    }
}
