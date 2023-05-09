package com.example.todo_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo_app.R;
import com.example.todo_app.databinding.ActivityUpdateNoteBinding;
import com.example.todo_app.model.Todo;
import com.example.todo_app.viewmodel.TodosViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class UpdateTodoActivity extends AppCompatActivity {

    ActivityUpdateNoteBinding binding;
    TodosViewModel todosViewModel;

    int intentId;
    String intentTitle;
    String intentPriority;
    String intentDescription;
    String priority = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        binding = ActivityUpdateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intentId = getIntent().getIntExtra("id", 0);
        intentTitle = getIntent().getStringExtra("title");
        intentDescription = getIntent().getStringExtra("description");
        intentPriority = getIntent().getStringExtra("priority");

        binding.titleUpdate.setText(intentTitle);
        binding.descriptionUpdate.setText(intentDescription);

        switch (intentPriority) {
            case "1":
                binding.highPriorityUpdate.setImageResource(R.drawable.ic_baseline_check_24);
                break;
            case "2":
                binding.mediumPriorityUpdate.setImageResource(R.drawable.ic_baseline_check_24);
                break;
            case "3":
                binding.lowPriorityUpdate.setImageResource(R.drawable.ic_baseline_check_24);
                break;
        }


        todosViewModel = new ViewModelProvider(this).get(TodosViewModel.class);

        binding.highPriorityUpdate.setOnClickListener(event -> {
            binding.highPriorityUpdate.setImageResource(R.drawable.ic_baseline_check_24);
            binding.mediumPriorityUpdate.setImageResource(0);
            binding.lowPriorityUpdate.setImageResource(0);
            priority = "1";
        });

        binding.mediumPriorityUpdate.setOnClickListener(event -> {
            binding.highPriorityUpdate.setImageResource(0);
            binding.mediumPriorityUpdate.setImageResource(R.drawable.ic_baseline_check_24);
            binding.lowPriorityUpdate.setImageResource(0);
            priority = "2";
        });

        binding.lowPriorityUpdate.setOnClickListener(event -> {
            binding.highPriorityUpdate.setImageResource(0);
            binding.mediumPriorityUpdate.setImageResource(0);
            binding.lowPriorityUpdate.setImageResource(R.drawable.ic_baseline_check_24);
            priority = "3";
        });


        binding.updateTodosBtn.setOnClickListener(event -> {
            String title = binding.titleUpdate.getText().toString();
            String description = binding.descriptionUpdate.getText().toString();

            if (title.isEmpty()) {
                binding.titleUpdate.setError("Please, fill title field");
            }

            if (description.isEmpty()) {
                binding.descriptionUpdate.setError("Please, fill description field");
            }

            if (!title.isEmpty() && !description.isEmpty()) {
                this.updateTodos(intentId, title, description);
            }

        });

    }

    public void updateTodos(int id, String title, String description) {
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", new Date().getTime());

        Todo todo = new Todo();
        todo.id = id;
        todo.title = title;
        todo.description = description;
        todo.date = sequence.toString();
        todo.priority = this.priority;
        todosViewModel.updateTodos_vm(todo);

        Toast.makeText(this, "Todo updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.delete_btn) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(UpdateTodoActivity.this);
            View view = LayoutInflater.from(UpdateTodoActivity.this).inflate(R.layout.delete_sheet, (LinearLayout) findViewById(R.id.delete_sheet_linear_layout));
            sheetDialog.setContentView(view);

            TextView yesBtn, noBtn;

            yesBtn = view.findViewById(R.id.delete_true);
            noBtn = view.findViewById(R.id.delete_false);

            yesBtn.setOnClickListener(event -> {
                todosViewModel.deleteTodos_vm(intentId);
                Toast.makeText(this, "Todo deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            });

            noBtn.setOnClickListener(event -> {
                sheetDialog.dismiss();
            });
            sheetDialog.show();
        }
        return true;
    }
}