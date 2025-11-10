package com.example.hw04_gymlog_v300.viewHolder;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hw04_gymlog_v300.database.GymLogRepsository;
import com.example.hw04_gymlog_v300.database.entites.GymLog;

import java.util.List;

public class GymLogViewModel extends AndroidViewModel {
    private final GymLogRepsository repsository;

    //private final LiveData<List<GymLog>> allLogsByID;

    public GymLogViewModel (Application application){
        super(application);
        repsository = GymLogRepsository.getReposoitory(application);
   //     allLogsByID = repsository.updateSharedPreferenceLiveData(userID);
    }

    public LiveData<List<GymLog>> getAllLogsByID(int userID) {
        return  repsository.updateSharedPreferenceLiveData(userID);
    }


    public void insert(GymLog log){
        repsository.insertGymLog(log);
    }
}
