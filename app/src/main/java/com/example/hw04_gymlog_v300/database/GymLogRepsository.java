package com.example.hw04_gymlog_v300.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.hw04_gymlog_v300.database.entites.GymLog;
import com.example.hw04_gymlog_v300.MainActivity;
import com.example.hw04_gymlog_v300.database.entites.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GymLogRepsository {

    private static GymLogRepsository repsository;


    private final GymLogDAO gymLogDAO;
    private final UserDAO userDAO;
    private ArrayList<GymLog> allLogs;
    private GymLogRepsository(Application application){
        GymLogDataBase db = GymLogDataBase.getDatabase(application);
        this.gymLogDAO = db.gymLogDAO();
        this.userDAO = db.userDao();
        this.allLogs = (ArrayList<GymLog>) this.gymLogDAO.getAllRecords();
    }
public static GymLogRepsository getReposoitory(Application application){
        if(repsository != null) {
            return repsository;
        }
        Future<GymLogRepsository> future = GymLogDataBase.databaseWriteExecutor.submit(new Callable<GymLogRepsository>() {
            @Override
            public GymLogRepsository call() throws Exception {
                return new GymLogRepsository(application);
            }
        }


        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.tag, "Problem getting GymLogRepository, thread error ");
        }
    return null;
}
    public ArrayList<GymLog> getAllLogs(){
        Future<ArrayList<GymLog>> future = GymLogDataBase.databaseWriteExecutor.submit(new Callable<ArrayList<GymLog>>() {
            @Override
            public ArrayList<GymLog> call() throws Exception {
                return (ArrayList<GymLog>) gymLogDAO.getAllRecords();
            }
        });
        try {
            return future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i(MainActivity.tag, "problem when getting all GymLogs in repository");
        }
        return null;
    }
    public void insertGymLog(GymLog gymlog){
        GymLogDataBase.databaseWriteExecutor.execute(() -> {
            gymLogDAO.insert(gymlog);
        });
    }

    public void insertUser(User... user){
        GymLogDataBase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {

        return userDAO.getUserByUserName(username);

    }

    public LiveData<User> getUserByUserID(int userID) {

        return userDAO.getUserByUserID(userID);

    }

    public ArrayList<GymLog>getAllLogsByUserID(int loggedInUserID) {

        Future<ArrayList<GymLog>> future = GymLogDataBase.databaseWriteExecutor.submit(new Callable<ArrayList<GymLog>>() {
            @Override
            public ArrayList<GymLog> call() throws Exception {
                return (ArrayList<GymLog>) gymLogDAO.getAllRecordsByUserID(loggedInUserID);
            }
        });
        try {
            return future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i(MainActivity.tag, "problem when getting all GymLogs in repository");
        }
        return null;


    }
}
