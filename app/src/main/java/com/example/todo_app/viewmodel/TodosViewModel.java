package com.example.todo_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo_app.model.Todo;
import com.example.todo_app.repository.TodosRepository;

import java.util.List;

public class TodosViewModel extends AndroidViewModel {

    TodosRepository repository;
    public LiveData<List<Todo>> getAllTodos;

    public TodosViewModel(@NonNull Application application) {
        super(application);

        repository = new TodosRepository(application);
        getAllTodos = repository.getAllTodos;
    }

    public void insertTodos_vm(Todo todo) {
        repository.insertTodos(todo);
    }

    public void updateTodos_vm(Todo todo) {
        repository.updateTodos(todo);
    }

    public void deleteTodos_vm(int id) {
        repository.deleteTodos(id);
    }
}
