package tpp.profixer.customer.ui.home;

import androidx.databinding.ObservableField;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    public ObservableField<String> title = new ObservableField<>("");
    public HomeViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
        getProfile();
    }
}
