package com.example.collegetransporttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transport.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Bus table
        db.execSQL("CREATE TABLE buses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bus_number TEXT," +
                "route TEXT," +
                "driver TEXT," +
                "phone TEXT)");

        // Route table
        db.execSQL("CREATE TABLE routes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "route_name TEXT," +
                "stops TEXT)");

        // Schedule table
        db.execSQL("CREATE TABLE schedules (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bus_number TEXT," +
                "route TEXT," +
                "departure TEXT," +
                "arrival TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS buses");
        db.execSQL("DROP TABLE IF EXISTS routes");
        db.execSQL("DROP TABLE IF EXISTS schedules");

        onCreate(db);
    }

    public boolean addBus(String busNumber, String route,
                          String driver, String phone) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("bus_number", busNumber);
        values.put("route", route);
        values.put("driver", driver);
        values.put("phone", phone);

        long result = db.insert("buses", null, values);

        return result != -1;
    }

    public boolean addRoute(String routeName, String stops) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("route_name", routeName);
        values.put("stops", stops);

        long result = db.insert("routes", null, values);

        return result != -1;
    }

    public boolean addSchedule(String busNumber, String route,
                               String departure, String arrival) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("bus_number", busNumber);
        values.put("route", route);
        values.put("departure", departure);
        values.put("arrival", arrival);

        long result = db.insert("schedules", null, values);

        return result != -1;
    }

}