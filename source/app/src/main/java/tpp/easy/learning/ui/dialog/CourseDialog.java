package tpp.easy.learning.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.databinding.DialogCourseBinding;
import tpp.easy.learning.ui.cart.CartActivity;

public class CourseDialog extends BottomSheetDialog {
    public ObservableField<Course> course = new ObservableField<>();

    @Getter
    @Setter
    private CourseListener listener;

    public interface CourseListener{
        void course();
    }
    public CourseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogCourseBinding binding = DialogCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        binding.setD(this);
        binding.executePendingBindings();

    }
    public void navigateToCart(){
        dismiss();
        Intent intent = new Intent(getContext(), CartActivity.class);
        getContext().startActivity(intent);
    }
}
