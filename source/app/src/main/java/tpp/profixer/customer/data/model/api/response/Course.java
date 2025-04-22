package tpp.profixer.customer.data.model.api.response;

import android.annotation.SuppressLint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long id;
    private Integer status;
    private String name;
    private Float averageStar;
    private Integer totalReview;
    private Integer totalLesson;
    private Integer totalStudyTime;
    private Integer soldQuantity;
    private String shortDescription;
    private String description;
    private String avatar;
    private String banner;
    private Integer price;
    private Integer saleOff;
    private Integer kind;
    private Boolean isSellerCourse;
    private Field field;
    private Expert expert;

    @SuppressLint("DefaultLocale")
    public String getTimeStudy(){
        if(totalStudyTime == null || totalStudyTime == 0){
            return "00:00";
        }
        int hours = totalStudyTime / 60;
        int minutes = totalStudyTime % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public Integer getMoney(){
        if(saleOff == null || saleOff == 0){
            return price;
        }
        return Math.round(price - ((float) (price * saleOff) / 100));
    }
}
