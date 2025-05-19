package tpp.profixer.customer.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Long id;
    private String modifiedDate;
    private String createdDate;
    private Long idUser;
    private Integer state;
    private Integer kind;
    private String msg;
}
