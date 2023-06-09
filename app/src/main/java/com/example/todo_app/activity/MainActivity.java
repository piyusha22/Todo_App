package com.example.todo_app.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView; //change

import android.widget.TextView;
import android.widget.Toast;

import com.example.todo_app.R;
import com.example.todo_app.activity.AddActivity;
import com.example.todo_app.activity.LoginActivity;

import com.example.todo_app.adapter.TodosAdapter;
import com.example.todo_app.model.Todo;
import com.example.todo_app.viewmodel.TodosViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addTodosBtn;
    TodosViewModel todosViewModel;
    RecyclerView recyclerView;
    TodosAdapter todosAdapter;
    SearchView searchView;
    LinearLayout todosNotFound;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Test", "Test log");
        String TAG = "First Log";
        Log.d(TAG, "My first log message");

        String username = getIntent().getStringExtra("username");
        username = username.replace(username.charAt(0), (username.charAt(0) + "").toUpperCase().charAt(0));
        setTitle("Hello, " + username);

        addTodosBtn = findViewById(R.id.add_todo);
        searchView = findViewById(R.id.search_view_bar);
        todosNotFound = findViewById(R.id.todos_not_found);
        todosViewModel = new ViewModelProvider(this).get(TodosViewModel.class);

        addTodosBtn.setOnClickListener(event -> {
            startActivity(new Intent(MainActivity.this, AddActivity.class));
        });

        recyclerView = findViewById(R.id.todos_recycler_view);

        todosViewModel.getAllTodos.observe(this, todos -> {

            //Logging all the added todos for debugging
            todos.forEach(todo -> {
                Log.d("TODO", "Created Todo : " + todo.title);
            });

            if (todos.size() > 0) {
                todosNotFound.setVisibility(View.GONE);
            }

            if (todos.size() <= 0 && todosNotFound.getVisibility() == View.GONE) {
                todosNotFound.setVisibility(View.VISIBLE);
            }
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            todosAdapter = new TodosAdapter(MainActivity.this, todos);
            recyclerView.setAdapter(todosAdapter);

        });

        /* Adding swipe to delete and drag and drop functionality using ItemTouchHelper */
        //for drag and drop feature
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                List<Todo> todos = todosViewModel.getAllTodos.getValue();

                if (todos != null) {
                    int fromPosition = viewHolder.getAdapterPosition();
                    int toPosition = target.getAdapterPosition();

                    Collections.swap(todos, fromPosition, toPosition);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
                    return true;
                }
                return false;
            }

            //for swipe to delete functionality
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<Todo> todos = todosViewModel.getAllTodos.getValue();

                BottomSheetDialog sheetDialog = new BottomSheetDialog(com.example.todo_app.activity.MainActivity.this);
                View view = LayoutInflater.from(com.example.todo_app.activity.MainActivity.this).inflate(R.layout.delete_sheet, (LinearLayout) findViewById(R.id.delete_sheet_linear_layout));
                sheetDialog.setContentView(view);
                sheetDialog.show();

                TextView yesBtn, noBtn;

                yesBtn = view.findViewById(R.id.delete_true);
                noBtn = view.findViewById(R.id.delete_false);

                yesBtn.setOnClickListener(event -> {
                    if (todos != null) {
                        int position = viewHolder.getAdapterPosition();
                        Todo todo = todos.get(position);
                        todosViewModel.deleteTodos_vm(todo.id);
                        Toast.makeText(com.example.todo_app.activity.MainActivity.this, "Todo deleted successfully", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(position);
                        sheetDialog.dismiss();
                    } else {
                        Toast.makeText(com.example.todo_app.activity.MainActivity.this, "Unable to find error", Toast.LENGTH_SHORT).show();
                        sheetDialog.dismiss();
                    }
                });

                noBtn.setOnClickListener(event -> {
                    sheetDialog.dismiss();
                });
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String s) {
                todosViewModel.getAllTodos.observe(MainActivity.this, todos -> {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
                    List<Todo> todoList = todos.stream().filter(todo -> todo.title.contains(s)).collect(Collectors.toList());
                    todosAdapter = new TodosAdapter(MainActivity.this, todoList);
                    recyclerView.setAdapter(todosAdapter);
                });
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.exit_menu) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(MainActivity.this);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.logout_sheet, (LinearLayout) findViewById(R.id.logout_sheet_linear_layout));
            sheetDialog.setContentView(view);

            TextView yesBtn, noBtn;

            yesBtn = view.findViewById(R.id.logout_true);
            noBtn = view.findViewById(R.id.logout_false);

            yesBtn.setOnClickListener(event -> {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "Logged Out Successfully !", Toast.LENGTH_SHORT).show();
            });

            noBtn.setOnClickListener(event -> {
                sheetDialog.dismiss();
            });

            sheetDialog.show();
        }
        return true;
    }
}