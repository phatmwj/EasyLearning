package tpp.profixer.customer.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgetRequest {
    private String email;
    private Integer accountKind = 4;
    private Integer app = 1;
}
