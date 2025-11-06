package com.example.hw04_gymlog_v300.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hw04_gymlog_v300.Database.entites.GymLog;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GymLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GymLog gymlog);
    @Query("Select * from " + GymLogDataBase.GYM_LOG_TABLE)
    ArrayList<GymLog> getAllRecords();
}
