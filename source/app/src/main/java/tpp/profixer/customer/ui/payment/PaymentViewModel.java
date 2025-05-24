package tpp.profixer.customer.ui.payment;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.model.api.response.CartInfo;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;

public class PaymentViewModel extends BaseViewModel {
//    public MutableLiveData<CartInfo> cartInfo = new MutableLiveData<>();
    public ObservableField<Integer> price = new ObservableField<>(0);
    public ObservableField<Integer> oldPrice = new ObservableField<>(0);
    public ObservableField<Integer> voucher = new ObservableField<>(0);
    public ObservableField<CartInfo> cartInfoF = new ObservableField<>();
    public PaymentViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }
}
