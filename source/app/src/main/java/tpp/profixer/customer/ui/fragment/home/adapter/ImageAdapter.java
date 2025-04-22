package tpp.profixer.customer.ui.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tpp.profixer.customer.BR;
import tpp.profixer.customer.data.model.app.Image;
import tpp.profixer.customer.databinding.ItemImageBinding;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Image> images;
    private Context context;

    public ImageAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemImageBinding binding = ItemImageBinding.inflate(inflater, parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ItemImageBinding itemImageBinding;
        public ImageViewHolder(@NonNull ItemImageBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void onBind(int position){
            itemImageBinding.setVariable(BR.image, images.get(position));
            itemImageBinding.executePendingBindings();
        }
    }

}
