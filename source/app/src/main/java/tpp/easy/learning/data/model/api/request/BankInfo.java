package tpp.easy.learning.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankInfo {
    private String accountNo;
    private String accountName;
    private String acqId = "970418";
    private String addInfo;
    private String amount;
//    "vietqr_pro"
    private String template = "SVGJ5lw";
}
