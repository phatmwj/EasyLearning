package tpp.easy.learning.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import tpp.easy.learning.databinding.DialogReviewBinding;

public class ReviewDialog extends Dialog {

    DialogReviewBinding binding;
    public ObservableField<Integer> star = new ObservableField<>(0);
    public ObservableField<String> msg = new ObservableField<>("");
    public ObservableField<Boolean> reviewed = new ObservableField<>(false);
    @Getter
    @Setter
    private ReviewListener listener;
    public ReviewDialog(@NonNull Context context) {
        super(context);
    }

    public interface ReviewListener{
        void onReview(Integer star, String msg);
        void onNoStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        binding.setD(this);
        binding.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                star.set((int) rating);
            }
        });
        msg.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(msg.get().trim().isEmpty()){
                    binding.layoutFullname.setError("Nhận xét không được để trống");
                }else {
                    binding.layoutFullname.setError(null);
                }
            }
        });
        binding.executePendingBindings();
    }

    public void review(){
        if (star.get() == 0){
            listener.onNoStart();
            return;
        }
        if(msg.get().trim().isEmpty()){
            binding.layoutFullname.setError("Nhận xét không được để trống");
            return;
        }else {
            binding.layoutFullname.setError(null);
        }
        listener.onReview(star.get(), msg.get());
    }
}
