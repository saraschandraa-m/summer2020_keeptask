package com.appstone.keeptask;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksHolder> implements Filterable {

    private Context context;
    private ArrayList<Task> taskList; // original arraylist
    private ArrayList<Task> displayList;    //display arraylist
    private ArrayList<Task> searchList;     //search filter arraylist
    private TaskUpdateListener listener;

    private TaskFilter filter;

    public TasksAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.displayList = (ArrayList<Task>) taskList.clone();
        searchList = new ArrayList<>();
        filter = new TaskFilter();
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
        final Task item = displayList.get(position);

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
                mTvTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mChItem.setChecked(false);
            }

            mTvTitle.setText(taskItem.itemName);
            mChItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                    listener.onTaskUpdate(taskItem, item, checked);
                }
            });

            holder.mLlDynamicViews.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class TaskFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            searchList.clear();
            if (charSequence != null) {
                //Run the for loop with the original list
                for (Task task : taskList) {
                    if (task.taskName.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        searchList.add(task);
                    } else {

                        ArrayList<TaskItem> taskItems = Task.convertItemsStringToArrayList(task.items);

                        for (TaskItem taskItem : taskItems) {
                            if (taskItem.itemName.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                searchList.add(task);
                            }
                        }
                    }

                }

                results.values = searchList;
                results.count = searchList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            displayList = (ArrayList<Task>) filterResults.values;
            notifyDataSetChanged(); //refreshing the adapter
        }
    }

    public void clearFilter() {
        displayList.clear();
        displayList = (ArrayList<Task>) taskList.clone();
        notifyDataSetChanged();
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
        void onTaskUpdate(TaskItem item, Task task, boolean checked);
    }
}
