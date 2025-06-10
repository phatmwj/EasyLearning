package tpp.easy.learning.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {
    private Long notificationId;
    private Long bookingId;
    private Integer bookingState;
    private String code;
}
