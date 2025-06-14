package tpp.easy.learning.ui.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Setter;
import tpp.easy.learning.BR;
import tpp.easy.learning.data.model.api.request.Slide;
import tpp.easy.learning.databinding.ItemImageBinding;
import tpp.easy.learning.generated.callback.OnClickListener;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Slide> images;
    private Context context;

    public ImageAdapter(Context context, List<Slide> images) {
        this.context = context;
        this.images = images;
    }

    @Setter
    private OnItemListener onItemListener;
    public interface OnItemListener {
        void onItemClick(Slide slide);
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
        int position;
        public ImageViewHolder(@NonNull ItemImageBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
            itemImageBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemListener != null){
                        onItemListener.onItemClick(images.get(position));
                    }
                }
            });
        }

        public void onBind(int position){
            this.position = position;
            itemImageBinding.setVariable(BR.image, images.get(position));
            itemImageBinding.executePendingBindings();
        }
    }

}
