package tpp.profixer.customer.ui.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.CustomLesson;
import tpp.profixer.customer.databinding.ItemLessonBinding;
import tpp.profixer.customer.databinding.ItemLessonTitleBinding;

public class LessonAdapter extends BaseExpandableListAdapter {
    private List<CustomLesson> data;
    private Context context;
    private LayoutInflater layoutInflater;
    public LessonAdapter(Context context, List<CustomLesson> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getGroupCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getLessons() != null ? data.get(groupPosition).getLessons().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getLessons().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return data.get(groupPosition).getLesson().getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return data.get(groupPosition).getLessons().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ItemLessonTitleBinding binding;
        if(convertView == null){
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lesson_title, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else {
            binding = (ItemLessonTitleBinding) convertView.getTag();
        }
        binding.setItem(data.get(groupPosition).getLesson());
        binding.executePendingBindings();
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemLessonBinding binding;
        if(convertView == null){
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lesson, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else {
            binding = (ItemLessonBinding) convertView.getTag();
        }
        binding.setItem(data.get(groupPosition).getLessons().get(childPosition));
        binding.executePendingBindings();
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
