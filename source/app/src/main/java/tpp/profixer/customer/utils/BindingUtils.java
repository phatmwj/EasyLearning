package tpp.profixer.customer.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import tpp.profixer.customer.BuildConfig;

public final class BindingUtils {
    @BindingAdapter("image_url")
    public static void setImageUrl(ImageView view, String url) {
        if (url == null){
            return;
        }
        if(url.contains("https:")){
            Glide.with(view.getContext())
                    .load(url)
                    .into(view);
            return;
        }
        Glide.with(view.getContext())
                .load(BuildConfig.MEDIA_URL + url)
                .into(view);
    }

    @BindingAdapter("image_res")
    public static void setImageRes(ImageView view, int url) {
        view.setImageResource(url);
    }

    @BindingAdapter("view_selected")
    public static void setViewSelected(View view, Boolean selected) {
        if(selected == null){
            view.setSelected(false);
            return;
        }
        view.setSelected(selected);
    }

    @BindingAdapter("view_visibility")
    public static void setVisibility(View view, Boolean isVisible) {
        if(isVisible == null || !isVisible){
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.GONE);
    }

    @BindingAdapter("text_currency")
    public static void formatCurrency(TextView textView, Integer price) {
        textView.setText(NumberUtils.formatCurrency(price));
    }
}
