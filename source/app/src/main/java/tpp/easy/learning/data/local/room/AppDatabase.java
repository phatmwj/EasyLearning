package tpp.easy.learning.data.local.room;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;
import androidx.sqlite.db.SupportSQLiteDatabase;

import tpp.easy.learning.data.local.room.dao.UserDao;
import tpp.easy.learning.data.model.room.UserEntity;

@Database(entities = {
        UserEntity.class},
        version = 4,
        autoMigrations = {
//                @AutoMigration(from = 2, to = 3),
//                @AutoMigration(from = 3, to = 4),
//                @AutoMigration(from = 2, to = 3,spec = AppDatabase.TwoThree.class),

        },
        exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();

    public static class AppDatabaseMigration implements AutoMigrationSpec {
        @Override
        public void onPostMigrate(@NonNull SupportSQLiteDatabase db) {
            AutoMigrationSpec.super.onPostMigrate(db);
        }
    }
}
