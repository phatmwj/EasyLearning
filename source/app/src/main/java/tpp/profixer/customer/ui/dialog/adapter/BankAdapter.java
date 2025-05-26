package tpp.profixer.customer.ui.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tpp.profixer.customer.data.model.api.response.DeepLink;
import tpp.profixer.customer.databinding.ItemBankBinding;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHolder> {

    private List<DeepLink> DeepLinkList;

    private OnItemClickListener onItemClickListener;
    private Context context;

    public BankAdapter(Context context){
        this.context = context;
    }
    public void setDeepLinkList(List<DeepLink> DeepLinkList){
        this.DeepLinkList = DeepLinkList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBankBinding binding = ItemBankBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BankViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return DeepLinkList != null ? DeepLinkList.toArray().length : 0;
    }

    public class BankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ItemBankBinding binding;
        private OnItemClickListener onItemClickListener;

        private DeepLink DeepLink;
        public BankViewHolder(@NonNull ItemBankBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.DeepLink = DeepLinkList.get(position);
            binding.setIvm(DeepLink);
           binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.itemClick(DeepLink);
        }
    }

    public interface OnItemClickListener{
        void itemClick(DeepLink DeepLink);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
