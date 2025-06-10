package tpp.easy.learning.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeepLink {
    private String appId;
    private String appLogo;
    private String appName;
    private String bankName;
    private Integer monthlyInstall;
    private String deeplink;
    private Integer autofill;
}
