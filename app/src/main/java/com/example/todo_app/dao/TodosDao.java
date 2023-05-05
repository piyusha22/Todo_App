package com.example.todo_app.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo_app.model.Todo;

import java.util.List;

@Dao
public interface TodosDao {

    @Query("SELECT * FROM todo")
    LiveData<List<Todo>> getAll();

    @Insert
    void insertTodos(Todo... todos);

    @Update
    void updateTodos(Todo... todos);

    @Query("DELETE FROM todo WHERE todo.id = :id")
    void deleteTodo(int id);
}
