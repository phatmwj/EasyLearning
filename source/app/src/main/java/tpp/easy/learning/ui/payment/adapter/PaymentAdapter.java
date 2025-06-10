package tpp.easy.learning.ui.payment.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Setter;
import tpp.easy.learning.data.model.api.response.CartItem;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.databinding.ItemPaymentBinding;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private Context context;
    private List<CartItem> data;
    private LayoutInflater layoutInflater;
    @Setter
    private PaymentListener listener;

    public PaymentAdapter(Context context, List<CartItem> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<CartItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public interface PaymentListener{
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPaymentBinding binding = ItemPaymentBinding.inflate(layoutInflater, parent, false);
        return new PaymentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        private ItemPaymentBinding binding;
        private int position;
        public PaymentViewHolder(@NonNull ItemPaymentBinding binding) {
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
            binding.setItem(data.get(position).getCourse());
            binding.oldMoney.setPaintFlags(binding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.executePendingBindings();
        }
    }
}
