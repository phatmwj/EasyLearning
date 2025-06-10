package tpp.easy.learning.ui.fragment.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import tpp.easy.learning.data.model.app.AccountBtn;
import tpp.easy.learning.databinding.ItemAccountBinding;

public class AccountAdapter extends BaseAdapter {
    private Context context;
    private List<AccountBtn> data;
    LayoutInflater layoutInflater;

    public AccountAdapter(Context context, List<AccountBtn> data){
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
        ItemAccountBinding binding;
        if(convertView == null){
            binding = ItemAccountBinding.inflate(layoutInflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (ItemAccountBinding) convertView.getTag();
        }
        binding.setItem(data.get(position));
        binding.executePendingBindings();
        return convertView;
    }
}
