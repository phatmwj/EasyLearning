package tpp.easy.learning.ui.home;

import androidx.databinding.ObservableField;

import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.ui.base.activity.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    public ObservableField<String> title = new ObservableField<>("");
    public ObservableField<Boolean> loginButton = new ObservableField<>(false);
    public ObservableField<Boolean> isSearch = new ObservableField<>(false);
    public ObservableField<String> textSearch = new ObservableField<>("");
    public ObservableField<Boolean> notApp = new ObservableField<>(false);
    public HomeViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
        getProfile();
    }
}
