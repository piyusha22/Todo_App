package com.example.todo_app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.R;
import com.example.todo_app.activity.MainActivity;
import com.example.todo_app.activity.UpdateTodoActivity;
import com.example.todo_app.model.Todo;

import java.util.List;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodosViewHolder> {

    MainActivity mainActivity;

    List<Todo> todos;

    public TodosAdapter(MainActivity mainActivity, List<Todo> todos) {
        this.mainActivity = mainActivity;
        this.todos = todos;
    }

    @NonNull
    @Override
    public TodosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodosViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.todos_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodosAdapter.TodosViewHolder holder, int position) {
        Todo todo = todos.get(position);

        switch (todo.priority) {
            case "1":
                holder.priority.setBackgroundResource(R.drawable.red_dot);
                break;
            case "2":
                holder.priority.setBackgroundResource(R.drawable.yellow_dot);
                break;
            case "3":
                holder.priority.setBackgroundResource(R.drawable.green_dot);
                break;
        }

        holder.title.setText(todo.title);
        holder.description.setText(todo.description);
        holder.date.setText(todo.date);

        holder.itemView.setOnClickListener(event -> {
            Intent intent = new Intent(mainActivity, UpdateTodoActivity.class);
            intent.putExtra("id", todo.id);
            intent.putExtra("title", todo.title);
            intent.putExtra("description", todo.description);
            intent.putExtra("priority", todo.priority);
            mainActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    static class TodosViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, description;
        View priority;

        public TodosViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.todosTitle);
            description = itemView.findViewById(R.id.todosDescription);
            date = itemView.findViewById(R.id.todosDate);
            priority = itemView.findViewById(R.id.priority);
        }
    }
}
