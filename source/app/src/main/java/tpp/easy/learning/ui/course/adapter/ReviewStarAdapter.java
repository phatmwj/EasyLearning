package tpp.easy.learning.ui.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import lombok.Setter;
import tpp.easy.learning.data.model.api.response.AmountReview;
import tpp.easy.learning.databinding.ItemReviewStarBinding;

public class ReviewStarAdapter extends BaseAdapter {

    private Context context;
    private List<AmountReview> data;
    private LayoutInflater layoutInflater;
    @Setter
    private Integer totalReview;

    public ReviewStarAdapter(Context context, List<AmountReview> data, Integer totalReview) {
        this.context = context;
        this.data = data;
        this.totalReview = totalReview;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data != null ?  data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getStar().longValue();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemReviewStarBinding binding;
        if(convertView == null){
            binding = ItemReviewStarBinding.inflate(layoutInflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (ItemReviewStarBinding) convertView.getTag();
        }
        binding.setItem(data.get(position));
        binding.setTotalReview(totalReview);
        binding.executePendingBindings();
        return convertView;
    }
}
