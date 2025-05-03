package tpp.profixer.customer.ui.fragment.income;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tpp.profixer.customer.BR;
import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.FragmentIncomeBinding;
import tpp.profixer.customer.di.component.FragmentComponent;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;
import tpp.profixer.customer.ui.category.CategoryActivity;
import tpp.profixer.customer.ui.fragment.income.adapter.CategoryAdapter;

public class IncomeFragment extends BaseFragment<FragmentIncomeBinding, IncomeFragmentViewModel> {
    private CategoryAdapter categoryAdapter;
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_income;
    }

    @Override
    protected void performDataBinding() {
        setLayoutCategory();
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setLayoutCategory(){
        categoryAdapter = new CategoryAdapter(getContext(), ProFixerApplication.categories);
        binding.lvCategory.setAdapter(categoryAdapter);
        binding.lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CategoryActivity.class);
                intent.putExtra("category_id", id);
                startActivity(intent);
            }
        });
    }
}
