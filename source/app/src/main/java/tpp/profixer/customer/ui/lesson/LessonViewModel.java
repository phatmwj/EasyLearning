package tpp.profixer.customer.ui.lesson;

import androidx.databinding.ObservableField;

import tpp.profixer.customer.ProFixerApplication;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.ui.base.activity.BaseViewModel;

public class LessonViewModel extends BaseViewModel {
    public ObservableField<Boolean> isFullscreen = new ObservableField<>(false);
    public LessonViewModel(Repository repository, ProFixerApplication application) {
        super(repository, application);
    }
}
