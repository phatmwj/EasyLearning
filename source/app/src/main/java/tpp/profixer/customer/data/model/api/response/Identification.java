package tpp.profixer.customer.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identification {
    private String numberIdentification;
    private String placeIdentification;
    private String dateIdentification;
    private String introduction;
    private String shortInfo;
    private String cover;
}
