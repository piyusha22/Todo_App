package com.example.todo_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.example.todo_app.R;
import com.example.todo_app.databinding.ActivityAddBinding;
import com.example.todo_app.model.Todo;
import com.example.todo_app.viewmodel.TodosViewModel;

import java.util.Date;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;
    String title;
    String description;
    TodosViewModel todosViewModel;
    String priority = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        todosViewModel = new ViewModelProvider(this).get(TodosViewModel.class);

        binding.highPriority.setOnClickListener(event -> {
            binding.highPriority.setImageResource(R.drawable.ic_baseline_check_24);
            binding.mediumPriority.setImageResource(0);
            binding.lowPriority.setImageResource(0);
            priority = "1";
        });

        binding.mediumPriority.setOnClickListener(event -> {
            binding.highPriority.setImageResource(0);
            binding.mediumPriority.setImageResource(R.drawable.ic_baseline_check_24);
            binding.lowPriority.setImageResource(0);
            priority = "2";
        });

        binding.lowPriority.setOnClickListener(event -> {
            binding.highPriority.setImageResource(0);
            binding.mediumPriority.setImageResource(0);
            binding.lowPriority.setImageResource(R.drawable.ic_baseline_check_24);
            priority = "3";
        });

        binding.addTodosBtn.setOnClickListener(event -> {

            title = binding.todosTitle.getText().toString();
            description = binding.todosDescription.getText().toString();


            if (title.isEmpty()) {
                binding.todosTitle.setError("Please, fill title field");
            }

            if (description.isEmpty()) {
                binding.todosDescription.setError("Please, fill description field");
            }

            if (!title.isEmpty() && !description.isEmpty()) {
                this.createTodos(title, description);
            }
        });
    }

    public void createTodos(String title, String description) {

        CharSequence sequence = DateFormat.format("MMMM d, yyyy", new Date().getTime());

        Todo todo = new Todo();
        todo.title = title;
        todo.description = description;
        todo.date = sequence.toString();
        todo.priority = this.priority;
        todosViewModel.insertTodos_vm(todo);

        Toast.makeText(this, "Todos created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}