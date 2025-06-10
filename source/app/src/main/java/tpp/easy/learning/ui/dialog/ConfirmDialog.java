package tpp.easy.learning.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import tpp.easy.learning.databinding.DialogConfirmBinding;

public class ConfirmDialog extends Dialog {

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<Integer> iconRightButton = new ObservableField<>();
    public ObservableField<Integer> backgroundRightButton = new ObservableField<>();
    public ObservableField<String> titleRightButton = new ObservableField<>();
    public ObservableField<Integer> backgroundDialog = new ObservableField<>();
    @Getter
    @Setter
    private ConfirmListener listener;

    public interface ConfirmListener{
        void confirm();
    }
    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogConfirmBinding binding = DialogConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        binding.setD(this);
        binding.executePendingBindings();

    }
}
