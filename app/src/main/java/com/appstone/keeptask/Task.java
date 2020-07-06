package com.appstone.keeptask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Task {

    public int id;
    public String taskName;
    public String items;


//    public static ArrayList<TaskItem> convertItemsStringToArrayList(String items) {
//        ArrayList<TaskItem> taskItemList = new ArrayList<>();
//
//        try {
//            JSONArray itemsArray = new JSONArray(items);
//            for (int i = 0; i < itemsArray.length(); i++) {
//                JSONObject itemObject = itemsArray.getJSONObject(i);
//                TaskItem taskItem = new TaskItem();
//                taskItem.itemID = itemObject.getInt(TaskItem.CONST_ITEM_ID);
//                taskItem.itemName = itemObject.getString(TaskItem.CONST_ITEM_NAME);
//                taskItem.itemIsCompleted = itemObject.getBoolean(TaskItem.CONST_ITEM_COMPLETED);
//
//                taskItemList.add(taskItem);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return taskItemList;
//    }

//    public static String convertItemsListToString(ArrayList<TaskItem> taskItemList) {
//        String itemsArrayValue = "";
//
//        JSONArray itemsArray = new JSONArray();
//
//        for (TaskItem item : taskItemList) {
//            try {
//                JSONObject itemObject = new JSONObject();
//                itemObject.put(TaskItem.CONST_ITEM_ID, item.itemID);
//                itemObject.put(TaskItem.CONST_ITEM_NAME, item.itemName);
//                itemObject.put(TaskItem.CONST_ITEM_COMPLETED, item.itemIsCompleted);
//
//                itemsArray.put(itemObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        itemsArrayValue = itemsArray.toString();
//
//        return itemsArrayValue;
//    }


    /**
     * Retrieving from database
     * 1. The data is stored in the format of JSONArray String
     * 2. Retreive the string and convert it to JSONArray
     * 3. Then convert the JSONArray to ArrayList.
     */

    // "[{itemid : value, itemname:value, itemiscompleted:value}, {itemid: value1, itemname:value1, itemiscompleted:value1}]

    // get and opt in JSON
    public static ArrayList<TaskItem> convertItemsStringToArrayList(String items) {
        ArrayList<TaskItem> taskItems = new ArrayList<>();

        try {
            JSONArray itemsArray = new JSONArray(items);          //This step converts the String into a JSON Array.

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);        //This step creates each object from the JSON Array.
                TaskItem item = new TaskItem();
                item.itemID = itemObject.optInt(TaskItem.CONST_ITEM_ID);
                item.itemName = itemObject.optString(TaskItem.CONST_ITEM_NAME);
                item.itemIsCompleted = itemObject.optBoolean(TaskItem.CONST_ITEM_COMPLETED);

                taskItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return taskItems;
    }


    public static String convertItemsListToString(ArrayList<TaskItem> taskItems) {
        String items = "";
        JSONArray itemsArray = new JSONArray();

        for (TaskItem taskItem : taskItems) {
            try {
                JSONObject itemObject = new JSONObject();
                itemObject.put(TaskItem.CONST_ITEM_ID, taskItem.itemID);
                itemObject.put(TaskItem.CONST_ITEM_NAME, taskItem.itemName);
                itemObject.put(TaskItem.CONST_ITEM_COMPLETED, taskItem.itemIsCompleted);

                itemsArray.put(itemObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        items = itemsArray.toString();

        return items;
    }
}
