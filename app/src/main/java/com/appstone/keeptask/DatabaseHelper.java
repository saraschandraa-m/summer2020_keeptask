package com.appstone.keeptask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "tasks_table";
    private static final String COL_ID = "id";
    private static final String COL_CARD_NAME = "task_name";
    private static final String COL_ITEMS = "items";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_CARD_NAME + " TEXT," + COL_ITEMS + " TEXT)";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "tasks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertTask(SQLiteDatabase database, Task task) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CARD_NAME, task.taskName);
        cv.put(COL_ITEMS, task.items);
        database.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<Task> getTasksFromDatabase(SQLiteDatabase database) {
        ArrayList<Task> tasks = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Task item = new Task();
                item.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                item.items = cursor.getString(cursor.getColumnIndex(COL_ITEMS));
                item.taskName = cursor.getString(cursor.getColumnIndex(COL_CARD_NAME));

                tasks.add(item);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return tasks;
    }

    public void updateTask(SQLiteDatabase database, Task task) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CARD_NAME, task.taskName);
        cv.put(COL_ITEMS, task.items);

        database.update(TABLE_NAME, cv, COL_ID + "=" + task.id, null);
    }

    public void deleteTask(SQLiteDatabase database, Task task) {
        database.delete(TABLE_NAME, COL_ID + "=" + task.id, null);
    }
}
