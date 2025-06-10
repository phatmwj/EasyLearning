package tpp.easy.learning.data.model.api.request;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import tpp.easy.learning.BR;

public class LoginRequest extends BaseObservable {

    private String phone;
    private String password;
    private String grant_type = "student";

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
        notifyPropertyChanged(BR.grant_type);
    }
}


