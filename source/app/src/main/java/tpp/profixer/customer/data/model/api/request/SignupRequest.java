package tpp.profixer.customer.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String confirmPassword;
    private String email;
    private String password;
    private String phone;
    private String fullName;
    private String grant_type = "student";
    private String referralCode;
}
