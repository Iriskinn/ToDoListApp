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
        todoList = new LinkedList<>();
        weekList = new LinkedList<>();
        doneList = new LinkedList<>();
        changed = new LinkedList<>();


        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("taskTable", null, null, null, null, null, null);


        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            int descColIndex = c.getColumnIndex("description");
            int typeColIndex = c.getColumnIndex("type");
            int stateColIndex = c.getColumnIndex("status");
            int datetimeColIndex = c.getColumnIndex("date_time");

            do {
                String name = c.getString(nameColIndex);
                String description = c.getString(descColIndex);
                String type = c.getString(typeColIndex);
                int dateTime = c.getInt(datetimeColIndex);
                boolean state;
                if (c.getInt(stateColIndex) != 0) {
                    state = true;
                } else {
                    state = false;
                }

                Task newTask = new Task(name, description, type, state, dateTime);

                if (newTask.getStatus()) {
                    doneList.add(newTask);
                } else {
                    if (newTask.getType().equals("Week")) {
                        weekList.add(newTask);
                    } else if (newTask.getType().equals("ToDo")) {
                        todoList.add(newTask);
                    }
                }
            } while (c.moveToNext());
        } else {
            Log.d("Log: ", "0 rows");
        }
        c.close();
    }

    public static void addTask(Task task, Context context) {
        if (task.getType().equals("ToDo")) {
            todoList.add(task);
        } else if (task.getType().equals("Week")) {
            weekList.add(task);
        }

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

        db.insert("taskTable", null, cv);
    }

    public static void changeTask(Task task, boolean isChecked, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", task.getName());
        cv.put("date_time", task.getDateTime());
        cv.put("type", task.getType());
        cv.put("description", task.getDescription());
        if (isChecked) {
            cv.put("status", 1);
        } else {
            cv.put("status", 0);
        }

        int updCount = db.update("taskTable", cv, "date_time = ?",
                new String[] { Integer.toString(task.getDateTime()) });

        if (!task.getStatus() && isChecked) {
            task.setStatus(true);
            doneList.add(task);
            for (int i = 0; i < doneList.size(); i++) {
                if (doneList.get(i).getDateTime() >= task.getDateTime()) {
                    doneList.add(i, task);
                    break;
                }
            }
            if (task.getType().equals("Week")) {
                Data.weekList.remove(task);
            } else {
                Data.todoList.remove(task);
            }
            doneList.removeLast();
        }

        if (task.getStatus() && !isChecked) {
            task.setStatus(false);
            Data.doneList.remove(task);
            if (task.getType().equals("Week")) {
                weekList.add(task);
                for (int i = 0; i < weekList.size(); i++) {
                    if (weekList.get(i).getDateTime() >= task.getDateTime()) {
                        weekList.add(i, task);
                        break;
                    }
                }
                weekList.removeLast();
            } else {
                todoList.add(task);
                for (int i = 0; i <todoList.size(); i++) {
                    if (todoList.get(i).getDateTime() >= task.getDateTime()) {
                        todoList.add(i, task);
                        break;
                    }
                }
                todoList.removeLast();
            }
        }
        Log.d("Log; ", "updated rows count = " + updCount + " " + isChecked);
        dbHelper.close();
    }

    public static void deleteDoneTasks(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("taskTable", "status = 1", null);
    }

    public static LinkedList<Task> todoList;
    public static LinkedList<Task> weekList;
    public static LinkedList<Task> doneList;
    public static LinkedList<Task> changed;

    static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "TodoListAppDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("Log: ", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table taskTable ("
                    + "name text,"
                    + "description text,"
                    + "type text,"
                    + "date_time integer,"
                    + "status integer" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
