package tpp.easy.learning.data.local.room;

import javax.inject.Inject;

import tpp.easy.learning.data.local.room.dao.UserDao;

public class AppDbService implements RoomService {

    private final AppDatabase mAppDatabase;

    @Inject
    public AppDbService(AppDatabase appDatabase) {
        this.mAppDatabase = appDatabase;
    }


    @Override
    public UserDao userDao() {
        return mAppDatabase.getUserDao();
    }
}
