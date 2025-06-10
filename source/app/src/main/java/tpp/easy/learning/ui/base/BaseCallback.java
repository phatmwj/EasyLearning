package tpp.easy.learning.ui.base;

public interface BaseCallback {
    void onSuccess();
    void onFailed();
    void onError(Exception exception);
}
