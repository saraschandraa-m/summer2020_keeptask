package com.appstone.keeptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {


    private EditText mEtTaskTitle;
    private LinearLayout mLlDynamicHolder;
    private RelativeLayout mRlAddItem;
    private int row = 0;
    private ArrayList<TaskItem> taskItems;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar mToolbar = findViewById(R.id.tl_add_task);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Task");
        }

        mEtTaskTitle = findViewById(R.id.et_task_title);
        mLlDynamicHolder = findViewById(R.id.ll_dynamic_item);
        mRlAddItem = findViewById(R.id.rl_add_item);

        taskItems = new ArrayList<>();

        dbHelper = new DatabaseHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void onTaskAddClicked(View view) {
        String taskTitle = mEtTaskTitle.getText().toString();
        String items = Task.convertItemsListToString(taskItems);

        Task task = new Task();
        task.taskName = taskTitle;
        task.items = items;

        dbHelper.insertTask(dbHelper.getWritableDatabase(), task);

        setResult(Activity.RESULT_OK);
        finish();
    }


    public void onAddListItemClicked(View view) {
        mRlAddItem.setEnabled(false);

        row++;
        View editView = LayoutInflater.from(AddTaskActivity.this).inflate(R.layout.cell_task_edit, null);
        final EditText mEtTaskItem = editView.findViewById(R.id.et_task_item);
        ImageView mIvAddItem = editView.findViewById(R.id.iv_edit_done);

        mIvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItem newTaskItem = new TaskItem();
                newTaskItem.itemID = row;
                newTaskItem.itemName = mEtTaskItem.getText().toString();
                newTaskItem.itemIsCompleted = false;

                taskItems.add(newTaskItem);

                mRlAddItem.setEnabled(true);
            }
        });
        mLlDynamicHolder.addView(editView);
    }
}