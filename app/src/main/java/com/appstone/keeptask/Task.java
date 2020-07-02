package com.appstone.keeptask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Task {

    public int id;
    public String taskName;
    public String items;


    public static ArrayList<TaskItem> convertItemsStringToArrayList(String items) {
        ArrayList<TaskItem> taskItemList = new ArrayList<>();

        try {
            JSONArray itemsArray = new JSONArray(items);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                TaskItem taskItem = new TaskItem();
                taskItem.itemID = itemObject.getInt(TaskItem.CONST_ITEM_ID);
                taskItem.itemName = itemObject.getString(TaskItem.CONST_ITEM_NAME);
                taskItem.itemIsCompleted = itemObject.getBoolean(TaskItem.CONST_ITEM_COMPLETED);

                taskItemList.add(taskItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return taskItemList;
    }

    public static String convertItemsListToString(ArrayList<TaskItem> taskItemList) {
        String itemsArrayValue = "";

        JSONArray itemsArray = new JSONArray();

        for (TaskItem item : taskItemList) {
            try {
                JSONObject itemObject = new JSONObject();
                itemObject.put(TaskItem.CONST_ITEM_ID, item.itemID);
                itemObject.put(TaskItem.CONST_ITEM_NAME, item.itemName);
                itemObject.put(TaskItem.CONST_ITEM_COMPLETED, item.itemIsCompleted);

                itemsArray.put(itemObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        itemsArrayValue = itemsArray.toString();

        return itemsArrayValue;
    }
}
