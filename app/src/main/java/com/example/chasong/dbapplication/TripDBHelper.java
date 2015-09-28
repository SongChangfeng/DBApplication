package com.example.chasong.dbapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by chasong on 9/16/2015.
 */
public class TripDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "trip.db";
    public static final int DATABASE_VERSION = 2;

    public static abstract class TripListTable implements BaseColumns {
        public static final String TABLE_NAME = "trip_list";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INFO = "info";
    }

    public static abstract class TripTable implements BaseColumns {
        public static final String TABLE_NAME = "trip";
        public static final String COLUMN_TRIP_ID = "trip_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INFO = "info";
    }

    public static abstract class TripItemsTable implements BaseColumns {
        public static final String TABLE_NAME = "trip_items";
        public static final String COLUMN_TRIP_DAY_ID = "trip_day_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INFO = "info";
    }

    public TripDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();

        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TripListTable.TABLE_NAME +
                    " (" + TripListTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TripListTable.COLUMN_NAME + " VARCHAR NOT NULL, " +
                    TripListTable.COLUMN_INFO + " TEXT NOT NULL)");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TripTable.TABLE_NAME +
                    " (" + TripTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TripTable.COLUMN_TRIP_ID + " INTEGER, " +
                    TripTable.COLUMN_NAME + " VARCHAR NOT NULL, " +
                    TripTable.COLUMN_INFO + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + TripTable.COLUMN_TRIP_ID +
                    ") REFERENCES " + TripListTable.TABLE_NAME +
                    " (" + TripListTable._ID + "))");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TripItemsTable.TABLE_NAME +
                    " (" + TripItemsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TripItemsTable.COLUMN_TRIP_DAY_ID + " INTEGER, " +
                    TripItemsTable.COLUMN_NAME + " VARCHAR NOT NULL, " +
                    TripItemsTable.COLUMN_INFO + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + TripItemsTable.COLUMN_TRIP_DAY_ID +
                    ") REFERENCES " + TripTable.TABLE_NAME +
                    " (" + TripTable._ID + "))");

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (newVersion > oldVersion && newVersion == 2) {
//
//            db.execSQL("CREATE TABLE IF NOT EXISTS " + TripItemsTable.TABLE_NAME +
//                    " (" + TripItemsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    TripItemsTable.COLUMN_TRIP_DAY_ID + " INTEGER, " +
//                    TripItemsTable.COLUMN_NAME + " VARCHAR NOT NULL, " +
//                    TripItemsTable.COLUMN_INFO + " TEXT NOT NULL, " +
//                    "FOREIGN KEY (" + TripItemsTable.COLUMN_TRIP_DAY_ID +
//                    ") REFERENCES " + TripTable.TABLE_NAME +
//                    " (" + TripTable._ID + "))");
//        }

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
