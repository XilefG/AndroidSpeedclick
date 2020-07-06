package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class highscoreDatabase extends SQLiteOpenHelper {
    public highscoreDatabase(Context context) {
        super(context, "highscores", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE highscores (ID INTEGER PRIMARY KEY AUTOINCREMENT, gametype TEXT, time DOUBLE)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS highscores");
        onCreate(db);
    }

    public void addData(String gameType, double time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("gametype", gameType);
        contentValues.put("time", time);

        db.insert("highscores", null, contentValues);
    }

    public Cursor getData(String gameType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT time FROM highscores WHERE gametype = '" + gameType + "' ORDER BY time";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM highscores");
    }

    public void deleteRow(double time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {String.valueOf(time)};
        db.delete("highscores", "time = ?", selectionArgs);
    }

    public double getRecord(String gameType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT time FROM highscores WHERE gametype = '" + gameType + "' ORDER BY time";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        return data.getDouble(0);
    }

    public double getAverage(String gameType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT time FROM highscores WHERE gametype = '" + gameType + "'";
        Cursor data = db.rawQuery(query, null);
        int totalTimes = data.getCount();
        double totalValue = 0;
        for(int i=0; i<totalTimes; i++) {
            data.moveToNext();
            totalValue +=data.getDouble(0);
        }
        double average = totalValue/totalTimes;
        return average;
    }
}
