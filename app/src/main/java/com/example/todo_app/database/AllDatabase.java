package com.example.todo_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todo_app.dao.TodosDao;
import com.example.todo_app.dao.UserDao;
import com.example.todo_app.model.Todo;
import com.example.todo_app.model.User;

@Database(entities = {Todo.class, User.class}, version = 1)
public abstract class AllDatabase extends RoomDatabase {

    public abstract TodosDao todosDao();

    public abstract UserDao userDao();

    public static AllDatabase INSTANCE;

    public static AllDatabase getDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AllDatabase.class, "todo").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}