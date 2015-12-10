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

        // получить данные из SQLite
        DBHelper dbHelper = new DBHelper(context);
        // создаем объект для данных
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("taskTable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
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

        Log.d("Log: ", "--- Insert in mytable: ---");

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

        // вставляем запись и получаем ее ID
        long rowID = db.insert("taskTable", null, cv);
        Log.d("Log: ", "row inserted, ID = " + rowID);
    }

    public static void doTask(Task task) {
        Task newTask = task;
        newTask.setStatus(true);
        for(int i = 0; i < todoList.size(); i++) {
            if (todoList.get(i).getName().equals(newTask.getName())) {
                todoList.remove(i);
            }
        }

        for(int i = 0; i < weekList.size(); i++) {
            if (weekList.get(i).getName().equals(newTask.getName())) {
                weekList.remove(i);
            }
        }
        doneList.add(newTask);
    }

    public static LinkedList<Task> todoList;
    public static LinkedList<Task> weekList;
    public static LinkedList<Task> doneList;


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
