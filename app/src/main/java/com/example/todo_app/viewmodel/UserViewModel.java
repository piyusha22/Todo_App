package com.example.todo_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo_app.model.User;
import com.example.todo_app.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    UserRepository repository;
    public LiveData<List<User>> getAllUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        getAllUser = repository.getAllUser;
    }

    public void insertUser_vm(User user) {
        repository.insertUsers(user);
    }

    public void updateUser_vm(User user) {
        repository.updateUsers(user);
    }

    public void deleteUser_vm(int id) {
        repository.deleteUsers(id);
    }
}
