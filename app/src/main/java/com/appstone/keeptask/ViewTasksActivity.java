package com.appstone.keeptask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ViewTasksActivity extends AppCompatActivity implements TasksAdapter.TaskUpdateListener {

    private RecyclerView mRcTasks;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        mRcTasks = findViewById(R.id.rc_view_cards);
        mRcTasks.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));

        dbHelper = new DatabaseHelper(this);

        getDataFromDatabase();
    }

    public void onAddTaskClicked(View view) {
        startActivityForResult(new Intent(ViewTasksActivity.this, AddTaskActivity.class), 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            getDataFromDatabase();
        }
    }

    private void getDataFromDatabase() {
        ArrayList<Task> tasks = dbHelper.getTasksFromDatabase(dbHelper.getReadableDatabase());
        TasksAdapter adapter = new TasksAdapter(this, tasks);
        adapter.setListener(this);
        mRcTasks.setAdapter(adapter);
    }

    @Override
    public void onTaskUpdate(TaskItem item, Task task) {
        ArrayList<TaskItem> taskItems = Task.convertItemsStringToArrayList(task.items);

        for (TaskItem taskItemObj : taskItems) {
            if (taskItemObj.itemID == item.itemID) {
                taskItemObj.itemIsCompleted = true;
            }
        }

        String itemArrayValue = Task.convertItemsListToString(taskItems);

        Task updatedTask = new Task();
        updatedTask.id = task.id;
        updatedTask.taskName = task.taskName;
        updatedTask.items = itemArrayValue;

        dbHelper.updateTask(dbHelper.getWritableDatabase(), updatedTask);

        getDataFromDatabase();
    }
}