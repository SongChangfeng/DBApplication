package com.example.chasong.dbapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by chasong on 9/21/2015.
 */
public class TripItemsDBManager {
    private SQLiteDatabase db;
    private ArrayList<Integer> queryList;
    private int tripDayID;

    public TripItemsDBManager(Context context, int tripDayID) {
        TripDBHelper dbHelper = new TripDBHelper(context);
        this.tripDayID = tripDayID;

        db = dbHelper.getWritableDatabase();
        queryList = new ArrayList<Integer>();
    }

    public void add(TripDayItem tripDayItem) {
        ContentValues values = new ContentValues();
        values.put(TripDBHelper.TripItemsTable.COLUMN_TRIP_DAY_ID, tripDayID);
        values.put(TripDBHelper.TripItemsTable.COLUMN_NAME, tripDayItem.name);
        values.put(TripDBHelper.TripItemsTable.COLUMN_INFO, tripDayItem.info);

        db.insert(TripDBHelper.TripItemsTable.TABLE_NAME,
                null,
                values);
    }

    public void delete(int index) {
        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(queryList.get(index))};

        db.delete(TripDBHelper.TripItemsTable.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void update(int index, TripDayItem tripDayItem) {
        ContentValues valuse = new ContentValues();
        valuse.put(TripDBHelper.TripItemsTable.COLUMN_TRIP_DAY_ID, tripDayID);
        valuse.put(TripDBHelper.TripItemsTable.COLUMN_NAME, tripDayItem.name);
        valuse.put(TripDBHelper.TripItemsTable.COLUMN_INFO, tripDayItem.info);

        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(queryList.get(index))};

        db.update(TripDBHelper.TripItemsTable.TABLE_NAME,
                valuse,
                selection,
                selectionArgs);
    }

    public Cursor queryTheCursor() {
        String selection = "SELECT * FROM " + TripDBHelper.TripItemsTable.TABLE_NAME;
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

    public class TripDayItem {
        public String name;
        public String info;

        public TripDayItem() {

        }

        public TripDayItem(String name, String info) {
            this.name = name;
            this.info = info;
        }
    }
}
