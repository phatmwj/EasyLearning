package tpp.profixer.customer.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expert {
    private Long id;
    private String birthDate;
    private Account account;
    private String referralCode;
    private Boolean isSystemExpert;
    private String address;
    private Ward ward;
    private District district;
    private Province province;
    private String identification;
    private Integer ordering;
    private Boolean isOutstanding;
    private Integer totalCourse;
    private Integer totalLessonTime;
    private Integer totalStudent;
}
