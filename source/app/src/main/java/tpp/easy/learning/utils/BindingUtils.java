package tpp.easy.learning.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import io.github.glailton.expandabletextview.ExpandableTextView;
import tpp.easy.learning.BuildConfig;
import tpp.easy.learning.R;

public final class BindingUtils {
    @BindingAdapter("image_url")
    public static void setImageUrl(ImageView view, String url) {
        if (url == null){
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.banner));
            return;
        }
        if(url.contains("https:")){
            Glide.with(view.getContext())
                    .load(url)
                    .error(R.drawable.banner)
                    .placeholder(R.drawable.banner)
                    .into(view);
            return;
        }
        Glide.with(view.getContext())
                .load(BuildConfig.MEDIA_URL+"/v1/file/download" + url)
                .error(R.drawable.banner)
                .placeholder(R.drawable.banner)
                .into(view);
    }
        @BindingAdapter("avatar_url")
        public static void setAvatarUrl(ImageView view, String url) {
            if (url == null){
                view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.avatar));
                return;
            }
            if(url.contains("https:")){
                Glide.with(view.getContext())
                        .load(url)
                        .error(R.drawable.avatar)
                        .placeholder(R.drawable.avatar)
                        .into(view);
                return;
            }
            Glide.with(view.getContext())
                    .load(BuildConfig.MEDIA_URL+"/v1/file/download" + url)
                    .error(R.drawable.avatar)
                    .placeholder(R.drawable.avatar)
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
    @BindingAdapter("text_currency")
    public static void formatCurrency(TextView textView, Double price) {
        if(price == null){
            price = 0.0;
        }
        textView.setText(NumberUtils.formatCurrency(price.intValue()));
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

    @BindingAdapter("text_html")
    public static void textHtml(TextView textView, String html) {
        if(html == null){
            textView.setText("");
            return;
        }
        textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
    }

    @BindingAdapter("text_html")
    public static void textHtml(ExpandableTextView textView, String html) {
        if(html == null){
            textView.setText("");
            return;
        }
        textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
    }

    @BindingAdapter("require_field")
    public static void textHtml(TextInputLayout layout, Boolean isRequire) {
        if(isRequire){
            String hint = layout.getHint().toString();
            SpannableString spannable = new SpannableString(hint + " *");
            spannable.setSpan(
                    new ForegroundColorSpan(Color.RED),          // Màu đỏ
                    hint.length(),                               // Bắt đầu từ vị trí dấu *
                    hint.length() + 2,                           // Kết thúc sau dấu *
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            layout.setHint(spannable);
        }
    }

    @BindingAdapter({"imageBase64"})
    public static void setImageBase64(ImageView imageView, String imageBase64) {
            if (imageBase64 != null && !imageBase64.isEmpty()) {
                Bitmap bitmap = null;
                String base64String = imageBase64.split(",")[1];
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(bitmap);
            }
    }
}
