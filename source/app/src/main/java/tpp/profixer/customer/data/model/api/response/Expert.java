package tpp.profixer.customer.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tpp.profixer.customer.data.model.room.UserEntity;

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

    public UserEntity convertToEntity(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setFullName(account.getFullName());
        userEntity.setAvatar(account.getAvatar());
        userEntity.setKind(account.getKind());
        userEntity.setPhone(account.getPhone());
        userEntity.setEmail(account.getEmail());
        if(province != null) userEntity.setProvinceId(province.getId());
        if(ward != null) userEntity.setWardId(ward.getId());
        if(district != null) userEntity.setDistrictId(district.getId());
        userEntity.setAddress(address);
        return userEntity;
    }
}
