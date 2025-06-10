package tpp.easy.learning.ui.qrcode.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Setter;
import tpp.easy.learning.data.model.api.response.Transaction;
import tpp.easy.learning.databinding.ItemTransactionBinding;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Context context;
    private List<Transaction> data;
    private LayoutInflater layoutInflater;
    @Setter
    private TransactionListener listener;

    public TransactionAdapter(Context context, List<Transaction> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Transaction> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public interface TransactionListener{
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(layoutInflater, parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        private ItemTransactionBinding binding;
        private int position;
        public TransactionViewHolder(@NonNull ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            binding.layoutMain.setOnClickListener(v -> {
//                if(listener != null){
//                    listener.onItemClick(data.get(position));
//                }
//            });
//            binding.layoutDelete.setOnClickListener(v -> {
//                if(listener != null){
//                    listener.onItemDelete(data.get(position));
//                }
//            });
        }

        public void onBind(int position){
            this.position = position;
            binding.setItem(data.get(position));
            binding.oldMoney.setPaintFlags(binding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.executePendingBindings();
        }
    }
}
