package com.example.todo_app.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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

            if(todos.size() > 0) {
                todosNotFound.setVisibility(View.GONE);
            }

            if(todos.size() <= 0 && todosNotFound.getVisibility() == View.GONE) {
                todosNotFound.setVisibility(View.VISIBLE);
            }

            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            todosAdapter = new TodosAdapter(MainActivity.this, todos);
            recyclerView.setAdapter(todosAdapter);
        });

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
        if(item.getItemId() == R.id.exit_menu) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(MainActivity.this);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.logout_sheet, (LinearLayout) findViewById(R.id.logout_sheet_linear_layout));
            sheetDialog.setContentView(view);

            TextView yesBtn, noBtn;

            yesBtn = view.findViewById(R.id.logout_true);
            noBtn = view.findViewById(R.id.logout_false);

            yesBtn.setOnClickListener(event -> {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "Logged out successfully !", Toast.LENGTH_SHORT).show();
            });

            noBtn.setOnClickListener(event -> {
                sheetDialog.dismiss();
            });

            sheetDialog.show();
        }

        return true;
    }
}