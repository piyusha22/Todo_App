package com.example.todo_app.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo_app.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAll();

    @Insert
    void insertUser(User... user);

    @Update
    void updateUser(User... user);

    @Query("DELETE FROM user WHERE user.id = :id")
    void deleteUser(int id);
}
