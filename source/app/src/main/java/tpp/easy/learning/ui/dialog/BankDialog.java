package tpp.easy.learning.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Setter;
import tpp.easy.learning.data.model.api.response.DeepLink;
import tpp.easy.learning.databinding.DialogBankBinding;
import tpp.easy.learning.ui.dialog.adapter.BankAdapter;

public class BankDialog extends Dialog {

    public ObservableField<String> stringSearch = new ObservableField<>("");
    private List<DeepLink> deepLinks = new ArrayList<>();
    private BankAdapter bankAdapter;
    public BankDialog(@NonNull Context context, List<DeepLink> deepLinks) {
        super(context);
        this.deepLinks = deepLinks;
    }
    @Setter
    private OnItemBankListener onItemBankListener;
    public interface OnItemBankListener{
        void itemClick(DeepLink DeepLink);

        void hideKeyboard();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogBankBinding binding = DialogBankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.white);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        bankAdapter = new BankAdapter(getContext());
        bankAdapter.setDeepLinkList(deepLinks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcBank.setLayoutManager(layoutManager);
        binding.rcBank.setAdapter(bankAdapter);
        bankAdapter.setOnItemClickListener(new BankAdapter.OnItemClickListener() {
            @Override
            public void itemClick(DeepLink DeepLink) {
                onItemBankListener.itemClick(DeepLink);
                dismiss();
            }
        });

        stringSearch.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(stringSearch.get() == null || stringSearch.get().isEmpty()){
                    bankAdapter.setDeepLinkList(deepLinks);
                }else{
                    List<DeepLink> deepLinkList = new ArrayList<>();
                    for(DeepLink deepLink : deepLinks){
                        if (deepLink.getAppId().toLowerCase().contains(stringSearch.get()) || deepLink.getAppName().toLowerCase().contains(stringSearch.get())) {
                            deepLinkList.add(deepLink);
                        }
                    }
                    bankAdapter.setDeepLinkList(deepLinkList);
                }
            }
        });

        binding.setD(this);
        binding.executePendingBindings();

    }

    public void deleteTextSearch(){
        stringSearch.set("");
    }
    
    public void hideKeyboard(){
        onItemBankListener.hideKeyboard();
    }
}
