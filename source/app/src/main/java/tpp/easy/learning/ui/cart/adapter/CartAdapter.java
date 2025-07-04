package tpp.easy.learning.ui.cart.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import lombok.Setter;
import tpp.easy.learning.data.model.api.response.CartItem;
import tpp.easy.learning.databinding.ItemCartBinding;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> data;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private LayoutInflater layoutInflater;
    @Setter
    private CartListener listener;

    public CartAdapter(Context context, List<CartItem> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<CartItem> cartItems) {
        this.data = cartItems;
        notifyDataSetChanged();
    }

    public interface CartListener{
        void onItemClick(CartItem cartItem);
        void onItemDelete(CartItem cartItem);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(layoutInflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ItemCartBinding binding;
        private int position;
        public CartViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.layoutMain.setOnClickListener(v -> {
                if(listener != null){
                    listener.onItemClick(data.get(position));
                }
            });
            binding.layoutDelete.setOnClickListener(v -> {
                if(listener != null){
                    listener.onItemDelete(data.get(position));
                    viewBinderHelper.closeLayout(String.valueOf(data.get(position).getId()));
                }
            });
        }

        public void onBind(int position){
            this.position = position;
            viewBinderHelper.bind(binding.swipeRevealLayout, String.valueOf(data.get(position).getId()));
            binding.setItem(data.get(position).getCourse());
            binding.oldMoney.setPaintFlags(binding.oldMoney.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.executePendingBindings();
        }
    }
}
