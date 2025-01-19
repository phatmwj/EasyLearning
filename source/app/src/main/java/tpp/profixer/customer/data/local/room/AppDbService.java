package tpp.profixer.customer.data.local.room;

import javax.inject.Inject;

import tpp.profixer.customer.data.local.room.dao.UserDao;

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
