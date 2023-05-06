package com.example.todo_app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todo_app.dao.TodosDao;
import com.example.todo_app.database.AllDatabase;
import com.example.todo_app.model.Todo;

import java.util.List;

public class TodosRepository {

    public TodosDao todosDao;
    public LiveData<List<Todo>> getAllTodos;

    public TodosRepository(Application application) {
        AllDatabase database = AllDatabase.getDatabaseInstance(application);
        todosDao = database.todosDao();
        getAllTodos = todosDao.getAll();
    }

    public void insertTodos(Todo todo) {
        todosDao.insertTodos(todo);
    }

    public void updateTodos(Todo todo) {
        todosDao.updateTodos(todo);
    }

    public void deleteTodos(int id) {
        todosDao.deleteTodo(id);
    }
}
