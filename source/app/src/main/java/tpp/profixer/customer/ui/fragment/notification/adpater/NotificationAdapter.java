package tpp.profixer.customer.ui.fragment.notification.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Setter;
import tpp.profixer.customer.data.model.api.ApiModelUtils;
import tpp.profixer.customer.data.model.api.response.Msg;
import tpp.profixer.customer.data.model.api.response.Notification;
import tpp.profixer.customer.databinding.ItemNotificationBinding;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;
    private List<Notification> data;

    private LayoutInflater layoutInflater;
    @Setter
    private NotificationListener listener;

    public NotificationAdapter(Context context, List<Notification> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Notification> relatedNotifications) {
        this.data = relatedNotifications;
        notifyDataSetChanged();
    }

    public interface NotificationListener{
        void onNotificationClick(Notification Notification);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(layoutInflater, parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ItemNotificationBinding binding;
        private int position;
        public NotificationViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if(listener != null){
                    listener.onNotificationClick(data.get(position));
                }
            });
        }

        public void onBind(int position){
            this.position = position;
            binding.setItem(data.get(position));
            Msg msg = ApiModelUtils.fromJson(data.get(position).getMsg(), Msg.class);
            binding.setItemMsg(msg);
            binding.executePendingBindings();
        }
    }
}
