package com.example.jackg.appointmentapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.example.jackg.appointmentapp.Constants.TABLE_NAME;
import static com.example.jackg.appointmentapp.Constants.TITLE;
import static com.example.jackg.appointmentapp.Constants.TIME;
import static com.example.jackg.appointmentapp.Constants.DETAILS;
import static com.example.jackg.appointmentapp.Constants.DATE;

/**
 * Created by jackg on 06/04/2017.
 */

public class appointmentsData extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "appointments.db";
    private static final int DATABASE_VERSION = 10;     //assingning table name and the table version


    public appointmentsData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);      // table constructor
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID  // creating my table columns and their types
    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE +
    " TEXT NOT NULL," + DATE + " TEXT NOT NULL," + TIME + " INTEGER," + DETAILS + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);       // if table version changes, update table
        onCreate(db);
    }
}
