package tpp.easy.learning.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpertInfo {
    private Long id;
    private String fullName;
    private String avatar;
    private String cover;
    private String introduction;
    private String totalCourse;
    private String totalLessonTime;
    private String totalStudent;
    private Integer ordering;
    private Boolean isOutstanding;
    private String identification;
}
