package tpp.easy.learning.ui.email;

import androidx.databinding.ObservableField;

import tpp.easy.learning.ProFixerApplication;
import tpp.easy.learning.data.Repository;
import tpp.easy.learning.ui.base.activity.BaseViewModel;

public class EmailViewModel extends BaseViewModel {
    public ObservableField<String> email = new ObservableField<>();
    public EmailViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }
}
