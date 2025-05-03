package tpp.profixer.customer.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import tpp.profixer.customer.BuildConfig;
import tpp.profixer.customer.R;

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
                .load(BuildConfig.MEDIA_URL+"/v1/file/download" + url)
                .error(R.drawable.banner)
                .placeholder(R.drawable.banner)
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
        view.setVisibility(View.VISIBLE);
    }

    @BindingAdapter("text_currency")
    public static void formatCurrency(TextView textView, Integer price) {
        if(price == null){
            textView.setText("");
            return;
        }
        textView.setText(NumberUtils.formatCurrency(price));
    }

    @BindingAdapter("text_datetime")
    public static void formatCurrency(TextView textView, String datetime) {
        if(datetime == null){
            textView.setText("");
            return;
        }
        textView.setText(DateUtils.convertTimeFromUtcToLocalString(datetime));
    }

    @BindingAdapter("text_line_horizontal")
    public static void formatTextLineHorizontal(TextView textView, boolean a) {
        if(a){
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @BindingAdapter("icon_tint")
    public static void setIconTint(ImageView view, Boolean check) {
        if(check != null && check){
            view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.app_color),
                    PorterDuff.Mode.MULTIPLY);
            return;
        }
        view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.text),
                PorterDuff.Mode.MULTIPLY);
    }
}
