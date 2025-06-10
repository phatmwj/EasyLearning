package tpp.easy.learning.data.model.api.request;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import tpp.easy.learning.BR;

public class NewPassRequest extends BaseObservable {

    private String newPassword;
    private String confirmPassword;
    private String idHash;
    private String otp;

    @Bindable
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        notifyPropertyChanged(BR.newPassword);
    }

    @Bindable
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Bindable
    public String getIdHash() {
        return idHash;
    }

    public void setIdHash(String idHash) {
        this.idHash = idHash;
        notifyPropertyChanged(BR.idHash);
    }

    @Bindable
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
        notifyPropertyChanged(BR.otp);
    }
}
