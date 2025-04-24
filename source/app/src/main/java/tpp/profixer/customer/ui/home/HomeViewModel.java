package tpp.profixer.customer.ui.home;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    public HomeViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
        getProfile();
    }
}
