package com.example.chasong.dbapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by chasong on 9/16/2015.
 */
public class TripDBManager {
    private SQLiteDatabase db;
    private ArrayList<Integer> queryList;
    private int tripID;

    public TripDBManager(Context context, int tripID) {
        TripDBHelper dbHelper = new TripDBHelper(context);
        this.tripID = tripID;

        db = dbHelper.getWritableDatabase();
        queryList = new ArrayList<Integer>();
    }

    public void add(TripDay tripDay) {
        ContentValues values = new ContentValues();
        values.put(TripDBHelper.TripTable.COLUMN_TRIP_ID, tripID);
        values.put(TripDBHelper.TripTable.COLUMN_NAME, tripDay.name);
        values.put(TripDBHelper.TripTable.COLUMN_INFO, tripDay.info);

        db.insert(TripDBHelper.TripTable.TABLE_NAME,
                null,
                values);
    }

    public void delete(int index) {
        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(queryList.get(index))};

        db.delete(TripDBHelper.TripTable.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void update(int index, TripDay tripDay) {
        ContentValues valuse = new ContentValues();
        valuse.put(TripDBHelper.TripTable.COLUMN_TRIP_ID, tripID);
        valuse.put(TripDBHelper.TripTable.COLUMN_NAME, tripDay.name);
        valuse.put(TripDBHelper.TripTable.COLUMN_INFO, tripDay.info);

        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(queryList.get(index))};

        db.update(TripDBHelper.TripTable.TABLE_NAME,
                valuse,
                selection,
                selectionArgs);
    }

    public Cursor queryTheCursor() {
        String selection = "SELECT * FROM " + TripDBHelper.TripTable.TABLE_NAME +
                " WHERE " + TripDBHelper.TripTable.COLUMN_TRIP_ID + " LIKE " +
                String.valueOf(tripID);
        Cursor cursor = db.rawQuery(selection, null);

        queryList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            queryList.add(id);
        }

        return cursor;
    }

    public void closeDB() {
        db.close();
    }

    public int getID (int index) {
        return queryList.get(index);
    }

    public class TripDay {
        public String name;
        public String info;

        public TripDay() {

        }

        public TripDay(String name, String info) {
            this.name = name;
            this.info = info;
        }
    }
}
