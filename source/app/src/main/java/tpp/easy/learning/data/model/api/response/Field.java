package tpp.easy.learning.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    private Long id;
    private Integer status;
    private String modifiedDate;
    private String createdDate;
    private String name;
    private String description;
    private Integer kind;
    private Integer ordering;
}
