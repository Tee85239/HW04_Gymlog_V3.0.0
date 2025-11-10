package com.example.hw04_gymlog_v300.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hw04_gymlog_v300.database.entites.User;

import java.util.List;

@Dao
public interface UserDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + GymLogDataBase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUser();

    @Query("DELETE from " + GymLogDataBase.USER_TABLE) void deleteALL();

    @Query("SELECT * FROM " + GymLogDataBase.USER_TABLE + " WHERE username = :username ")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + GymLogDataBase.USER_TABLE + " WHERE id = :userID")
    LiveData<User> getUserByUserID(int userID);


}
