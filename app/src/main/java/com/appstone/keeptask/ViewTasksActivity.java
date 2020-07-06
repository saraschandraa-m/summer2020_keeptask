package com.appstone.keeptask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class ViewTasksActivity extends AppCompatActivity implements TasksAdapter.TaskUpdateListener {

    private RecyclerView mRcTasks;

    private DatabaseHelper dbHelper;

    private Menu menu;

    private TasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        Toolbar mToolbar = findViewById(R.id.tl_view_task);
        setSupportActionBar(mToolbar);

        EditText mEtSearch = findViewById(R.id.et_search_text);

        final ImageView mIvClearSearch = findViewById(R.id.iv_clear_search);

        mIvClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clearFilter();
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    if (adapter != null && adapter.getItemCount() > 0) {
                        mIvClearSearch.setVisibility(View.VISIBLE);
                        adapter.getFilter().filter(editable.toString());
                    }
                } else {
                    mIvClearSearch.setVisibility(View.GONE);
                }
            }
        });

        mRcTasks = findViewById(R.id.rc_view_cards);

        dbHelper = new DatabaseHelper(this);

        getDataFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_view_task, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);

        return true;
    }

    public void onAddTaskClicked(View view) {
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(true);
//        startActivityForResult(new Intent(ViewTasksActivity.this, AddTaskActivity.class), 1000);
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
        adapter = new TasksAdapter(this, tasks);
        adapter.setListener(this);
        mRcTasks.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        mRcTasks.setAdapter(adapter);
    }

    @Override
    public void onTaskUpdate(TaskItem item, Task task, boolean isChecked) {
        ArrayList<TaskItem> taskItems = Task.convertItemsStringToArrayList(task.items);

        for (TaskItem taskItemObj : taskItems) {
            if (taskItemObj.itemID == item.itemID) {
                taskItemObj.itemIsCompleted = isChecked;
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