package tpp.easy.learning.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String message;
    private Integer star;
    private Long courseId;
    private Long expertId;
    private Integer kind;
    private Long studentId;
}
