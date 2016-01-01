package com.sample.drawer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.LinkedList;

public class Data {
    public static void receive(Context context) {
        taskList = new LinkedList<>();
        doneList = new LinkedList<>();
        lists = new LinkedList<>();

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);


        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            int descColIndex = c.getColumnIndex("description");
            int typeColIndex = c.getColumnIndex("type");
            int stateColIndex = c.getColumnIndex("status");
            int datetimeColIndex = c.getColumnIndex("date_time");

            int rateColIndex = c.getColumnIndex("rate");
            int doneTimeColIndex = c.getColumnIndex("done");
            int listIDColIndex = c.getColumnIndex("list_id");
            int favColIndex = c.getColumnIndex("fav");

            do {
                String name = c.getString(nameColIndex);
                String description = c.getString(descColIndex);
                String type = c.getString(typeColIndex);
                int dateTime = c.getInt(datetimeColIndex);

                int rate = c.getInt(rateColIndex);
                int doneTime = c.getInt(doneTimeColIndex);
                int listID = c.getInt(listIDColIndex);
                int fav = c.getInt(favColIndex);

                boolean state = c.getInt(stateColIndex) != 0;

                Task newTask = new Task(name, description, type, state, dateTime,
                        rate, doneTime, listID, fav);

                taskList.add(newTask);

            } while (c.moveToNext());
        } else {
            Log.d("Log: ", "0 tasks");
        }
        c.close();

        c = db.query(LIST_TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int dateTimeColIndex = c.getColumnIndex("date_time");

            do {
                String name = c.getString(nameColIndex);
                int id = c.getInt(idColIndex);
                int dateTime = c.getInt(dateTimeColIndex);

                TaskList newList = new TaskList(id, name, dateTime);

                lists.add(newList);

            } while (c.moveToNext());
        } else {
            Log.d("Log: ", "0 lists");
        }
        c.close();
    }

    public static void addTask(Task task, Context context) {

        ContentValues cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("name", task.getName().toString());
        cv.put("description", task.getDescription().toString());
        cv.put("type", task.getType().toString());
        cv.put("date_time", task.getDateTime());
        int status;
        if (task.getStatus()) {
            status = 1;
        } else {
            status = 0;
        }
        cv.put("status", status);
        cv.put("rate", task.getRate());
        cv.put("done", task.getDoneTime());
        cv.put("list_id", task.getListID());
        cv.put("fav", 0);

        db.insert(TABLE_NAME, null, cv);
    }

    public static void delTask(Task task, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(TABLE_NAME, "date_time = " + Integer.toString(task.getDateTime()), null);
    }

    public static void changeTask(Task task, boolean newStatus, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", task.getName());
        cv.put("date_time", task.getDateTime());
        cv.put("type", task.getType());
        cv.put("description", task.getDescription());
        if (newStatus) {
            cv.put("status", 1);
            cv.put("done", (int) new Date().getTime());
        } else {
            cv.put("status", 0);
            cv.put("done", 0);
        }

        int updCount = db.update(TABLE_NAME, cv, "date_time = ?",
                new String[] { Integer.toString(task.getDateTime()) });

        if (!task.getStatus() && newStatus) {
            task.setStatus(true);
        }

        if (task.getStatus() && !newStatus) {
            task.setStatus(false);
            Data.doneList.remove(task);

        }
        //Log.d("Log; ", "updated rows count = " + updCount + " " + isChecked);
        dbHelper.close();
    }

    public static void deleteDoneTasks(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "status = 1", null);

        for (Task task : taskList) {
            if (task.getStatus()) {
                taskList.remove(task);
            }
        }
    }

    public static void addList(TaskList list, Context context) {
        ContentValues cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("id", list.getID());
        cv.put("name", list.getName().toString());
        cv.put("date_time", list.getDateTime());

        db.insert(LIST_TABLE_NAME, null, cv);

        Data.lists.add(list);
    }

    public static void deleteList(int listID, Context context) {
        lists.remove(listID);

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        for (Task task : taskList) {
            if (task.getListID() == listID) {
                taskList.remove(task);

                db.delete(TABLE_NAME, "date_time = " + Integer.toString(task.getDateTime()), null);
            }
        }

        db.delete(LIST_TABLE_NAME, "id = " + Integer.toString(listID), null);
    }


    public static LinkedList<Task> taskList;
    public static LinkedList<Task> doneList;
    public static LinkedList<TaskList> lists;

    public static final String TABLE_NAME = "taskTable";
    public static final String LIST_TABLE_NAME = "taskLists";


    static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "TodoListAppDB", null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("Log: ", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table " + TABLE_NAME + " ("
                    + "name text, "
                    + "description text, "
                    + "type text, "
                    + "date_time integer, "
                    + "status integer, "
                    + "rate integer, "
                    + "done integer, "
                    + "list_id integer, "
                    + "fav integer" + ");");

            db.execSQL("create table " + LIST_TABLE_NAME + " ("
                    + "id integer, "
                    + "name text, "
                    + "date_time integer" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Log: ", "--- onUpgrade database ---");
        }
    }
}