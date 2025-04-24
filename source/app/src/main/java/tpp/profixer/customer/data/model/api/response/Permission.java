package tpp.profixer.customer.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private Integer status;
    private Long id;
    private String name;
    private String action;
    private Boolean showMenu;
    private String description;
    private String nameGroup;
    private String pcode;
}
