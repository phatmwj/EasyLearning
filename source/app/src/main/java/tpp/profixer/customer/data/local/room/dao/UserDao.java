package tpp.profixer.customer.data.local.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import tpp.profixer.customer.data.model.room.UserEntity;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity UserEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserEntity> userEntities);

    @Query("SELECT * FROM user")
    List<UserEntity> loadAll();

    @Query("SELECT * FROM user")
    LiveData<List<UserEntity>> loadAllToLiveData();

    @Query("SELECT * FROM user WHERE id=:id")
    LiveData<UserEntity> findById(long id);

    @Query("delete FROM user")
    void delete();

}
