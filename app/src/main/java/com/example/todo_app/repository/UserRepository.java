package com.example.todo_app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todo_app.dao.UserDao;
import com.example.todo_app.database.AllDatabase;
import com.example.todo_app.model.User;

import java.util.List;

public class UserRepository {

    public UserDao userDao;
    public LiveData<List<User>> getAllUser;

    public UserRepository(Application application) {
        AllDatabase database = AllDatabase.getDatabaseInstance(application);
        userDao = database.userDao();
        getAllUser = userDao.getAll();
    }

    public void insertUsers(User User) {
        userDao.insertUser(User);
    }

    public void updateUsers(User User) {
        userDao.updateUser(User);
    }

    public void deleteUsers(int id) {
        userDao.deleteUser(id);
    }
}
