package tpp.profixer.customer.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slide {
    private Long id;
    private Integer status;
    private String modifiedDate;
    private String createdDate;
    private String title;
    private String description;
    private String image;
    private String mobileImage;
    private String url;
    private Integer action;
}
