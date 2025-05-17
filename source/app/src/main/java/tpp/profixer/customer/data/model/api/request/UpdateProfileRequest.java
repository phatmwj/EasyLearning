package tpp.profixer.customer.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private String avatarPath;
    private String address;
    private String confirmPassword;
    private String email;
    private String fullName;
    private String newPassword;
    private String oldPassword;
    private String phone;
    private Long provinceId;
    private Long wardId;
    private Long districtId;


}
