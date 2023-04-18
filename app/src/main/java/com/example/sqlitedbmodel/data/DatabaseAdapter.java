package com.example.sqlitedbmodel.data;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    DatabaseHelper databaseHelper; //Позволяет получить доступ к базам данных
    SQLiteDatabase database;


    public DatabaseAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = databaseHelper.getReadableDatabase();
        return DatabaseAdapter.this;
    }

    public void close(){
        databaseHelper.close();
    }

    //вернем все записи таблицы TABLE_USERS
    private Cursor getUsersEntries(){
        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AGE};
        return database.query(DatabaseHelper.TABLE_USERS, columns, null, null, null, null, null);
    }

    //Создадим коллекцию наших User'ов
    public List<User> getUsers(){
        ArrayList<User> users = new ArrayList<User>();
        Cursor userCursor = getUsersEntries();
        while (userCursor.moveToNext()){
            @SuppressLint("Range") int id = userCursor.getInt(userCursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            @SuppressLint("Range") String name = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.TABLE_USERS));
            @SuppressLint("Range") String age = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_AGE));
            users.add(new User(id, name, age));
        }
        userCursor.close();
        return users;
    }

    //Найдем конкретного User'а
    public User getUser(long id){
        User receivedUser = null;
        String query = String.format("SELECT * FROM %s WHERE %s = ?", DatabaseHelper.TABLE_USERS, DatabaseHelper.COLUMN_ID);
        Cursor userCursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        while (userCursor.moveToFirst()){
            @SuppressLint("Range") String name = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.TABLE_USERS));
            @SuppressLint("Range") String age = userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_AGE));
            receivedUser = new User(id, name, age);
        }
        userCursor.close();
        return receivedUser;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_USERS);
    }

    public long insert(User user){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TABLE_USERS, user.getName());
        cv.put(DatabaseHelper.COLUMN_AGE, user.getAge());
        return database.insert(DatabaseHelper.TABLE_USERS, null, cv);
    }

    public long delete(long id){
        String condition = "_id = ?";
        String[] conditionsArgs = new String[]{String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE_USERS, condition, conditionsArgs);
    }

    public long update (User user){
        String whereClause = DatabaseHelper.COLUMN_ID + "=" + user.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, user.getName());
        cv.put(DatabaseHelper.COLUMN_AGE, user.getAge());
        return database.update(DatabaseHelper.TABLE_USERS, cv, whereClause, null);
    }
}
