package tpp.easy.learning.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Long id;
    private String message;
    private Expert studentInfo;
    private Course courseInfo;
    private Integer kind;
    private Integer star;
    private Integer status;
    private String createdDate;
}
