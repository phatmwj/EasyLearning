package tpp.easy.learning.data.model.app;

import androidx.room.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBtn {
    private Integer id;
    private String name;
    private Integer icon;
}
