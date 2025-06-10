package tpp.easy.learning.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tpp.easy.learning.data.model.room.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private String fullName;
    private String avatar;
    private Integer kind;
    private String phone;
    private String email;
    private Group group;
}
