package com.example.chasong.dbapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chasong on 9/16/2015.
 */
public class TripListDBManager {
    private SQLiteDatabase db;
    private ArrayList<Integer> queryList;

    public TripListDBManager(Context context) {
        TripDBHelper dbHelper = new TripDBHelper(context);

        db = dbHelper.getWritableDatabase();
        queryList = new ArrayList<Integer>();
    }

    public void add(Trip trip) {
        ContentValues values = new ContentValues();
        values.put(TripDBHelper.TripListTable.COLUMN_NAME, trip.name);
        values.put(TripDBHelper.TripListTable.COLUMN_INFO, trip.info);

        db.insert(TripDBHelper.TripListTable.TABLE_NAME,
                null,
                values);
    }

    public void delete(int index) {
        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(queryList.get(index))};

        db.delete(TripDBHelper.TripListTable.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void update(int index, Trip trip) {
        ContentValues valuse = new ContentValues();
        valuse.put(TripDBHelper.TripListTable.COLUMN_NAME, trip.name);
        valuse.put(TripDBHelper.TripListTable.COLUMN_INFO, trip.info);

        String selection = TripDBHelper.TripListTable._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(queryList.get(index))};

        db.update(TripDBHelper.TripListTable.TABLE_NAME,
                valuse,
                selection,
                selectionArgs);
    }

    public Cursor queryTheCursor() {
        String selection = "SELECT * FROM " + TripDBHelper.TripListTable.TABLE_NAME;
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

    public class Trip {
        public String name;
        public String info;

        public Trip(){

        }

        public Trip(String name, String info) {
            this.name = name;
            this.info = info;
        }
    }
}
