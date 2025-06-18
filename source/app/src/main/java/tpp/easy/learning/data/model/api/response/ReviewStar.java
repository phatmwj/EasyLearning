package tpp.easy.learning.data.model.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStar {
    private Integer kind;
    private Float averageStar;
    private Integer total;
    private List<AmountReview> amountReview;
    public String reviewStarD(){
        return String.valueOf(Math.round(averageStar*10)/10.0);
    }
}
