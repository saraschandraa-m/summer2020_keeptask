package com.appstone.keeptask;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksHolder> {

    private Context context;
    private ArrayList<Task> taskList;
    private TaskUpdateListener listener;

    public TasksAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    public void setListener(TaskUpdateListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public TasksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TasksHolder(LayoutInflater.from(context).inflate(R.layout.cell_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksHolder holder, int position) {
        final Task item = taskList.get(position);

        holder.mTvTaskTitle.setText(item.taskName);

        ArrayList<TaskItem> taskItems = Task.convertItemsStringToArrayList(item.items);

        holder.mLlDynamicViews.removeAllViews();
        for (final TaskItem taskItem : taskItems) {
            View view = LayoutInflater.from(context).inflate(R.layout.cell_task_view, null);
            TextView mTvTitle = view.findViewById(R.id.tv_task_item);
            CheckBox mChItem = view.findViewById(R.id.chk_view);
            RelativeLayout mRoot = view.findViewById(R.id.rl_view_root);
            if (taskItem.itemIsCompleted) {
                mChItem.setChecked(true);
                mChItem.setFocusable(false);
                mChItem.setFocusableInTouchMode(false);
                mTvTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mRoot.setFocusable(false);
                mRoot.setFocusableInTouchMode(false);
            }

            mTvTitle.setText(taskItem.itemName);
            mChItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked) {
                        listener.onTaskUpdate(taskItem, item);
                    }
                }
            });

            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTaskUpdate(taskItem, item);
                }
            });
            holder.mLlDynamicViews.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksHolder extends RecyclerView.ViewHolder {

        private TextView mTvTaskTitle;
        private LinearLayout mLlDynamicViews;

        public TasksHolder(@NonNull View itemView) {
            super(itemView);
            mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
            mLlDynamicViews = itemView.findViewById(R.id.ll_dyanmic_view);
        }
    }

    public interface TaskUpdateListener {
        void onTaskUpdate(TaskItem item, Task task);
    }
}
