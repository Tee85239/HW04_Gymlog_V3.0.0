package com.example.hw04_gymlog_v300.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.hw04_gymlog_v300.database.entites.GymLog;
import com.example.hw04_gymlog_v300.MainActivity;
import com.example.hw04_gymlog_v300.database.entites.User;
import com.example.hw04_gymlog_v300.database.typeConverter.LocalDateTypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {GymLog.class, User.class}, version = 1 , exportSchema = false)
public abstract class GymLogDataBase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    private static final String DATABASE_NAME = "GymLogDatabase";

    public static final String GYM_LOG_TABLE = "gymLogTable";

    private static volatile GymLogDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static GymLogDataBase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (GymLogDataBase.class){
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GymLogDataBase.class, DATABASE_NAME).fallbackToDestructiveMigration().addCallback(addDefaultValues).build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback addDefaultValues = new Callback(){
    @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        Log.i(MainActivity.tag, "Database created");
        databaseWriteExecutor.execute(() -> {
            UserDAO dao = INSTANCE.userDao();
            // dao.deleteALL();
            User admin = new User("admin1", "admin1");
            admin.setAdmin(true);
            dao.insert(admin);

            User testUser1 = new User("testUser1", "testUser1");
            testUser1.setAdmin(false);
            dao.insert(testUser1);
        });


    }

    };

    //If wrong watch vid 3
    public abstract GymLogDAO gymLogDAO() ;

    public abstract UserDAO userDao();

}
