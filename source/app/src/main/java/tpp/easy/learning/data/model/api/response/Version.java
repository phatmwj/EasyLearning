package tpp.easy.learning.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    private Long id;
    private Integer status;
    private String modifiedDate;
    private String createdDate;
    private Long courseId;
    private String date;
    private Integer versionCode;
    private Integer state;
    private String reviewNote;
}
