package tpp.profixer.customer.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tpp.profixer.customer.data.model.room.UserEntity;

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

    public UserEntity convertToEntity(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setFullName(fullName);
        userEntity.setAvatar(avatar);
        userEntity.setKind(kind);
        userEntity.setPhone(phone);
        userEntity.setEmail(email);
        return userEntity;
    }
}
