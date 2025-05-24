package tpp.profixer.customer.ui.email;

import androidx.databinding.ObservableField;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;

public class EmailViewModel extends BaseViewModel {
    public ObservableField<String> email = new ObservableField<>();
    public EmailViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }
}
