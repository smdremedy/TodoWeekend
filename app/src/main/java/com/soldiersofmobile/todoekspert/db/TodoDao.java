package com.soldiersofmobile.todoekspert.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.soldiersofmobile.todoekspert.Todo;

public class TodoDao {
    /**
     * Nazwy kolumn w DB.
     */
    public static final String C_ID = "_id";
    public static final String C_CONTENT = "content";
    public static final String C_DONE = "done";
    public static final String C_USER_ID = "user_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_UPDATED_AT = "updated_at";

    /**
     * Nazwa tabeli, w której przechowywane będa obiekty
     */
    public static final String TABLE_NAME = "todos";

    private DbHelper dbHelper;

    public TodoDao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(Todo todo, String userId) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_ID, todo.getObjectId());
        values.put(C_DONE, todo.isDone());
        values.put(C_CONTENT, todo.getContent());
        values.put(C_USER_ID, userId);

        database.insertWithOnConflict(TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);

    }


}
