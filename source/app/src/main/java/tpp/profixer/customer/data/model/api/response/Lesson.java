package tpp.profixer.customer.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private Long id;
    private Integer status;
    private String name;
    private Integer kind;
    private Integer ordering;
    private Boolean isDone;
    private Integer videoDuration;
    private Integer state;
    private Boolean isPreview;
}
