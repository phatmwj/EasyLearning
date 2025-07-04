package tpp.easy.learning.data.model.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "user")
public class UserEntity {
    @PrimaryKey
    @NonNull
    private Long id;
    @ColumnInfo(name = "full_name")
    private String fullName;
    private String avatar;
    private Integer kind;
    private String phone;
    private String email;
    private Long provinceId;
    private Long wardId;
    private Long districtId;
    private String address;
}
