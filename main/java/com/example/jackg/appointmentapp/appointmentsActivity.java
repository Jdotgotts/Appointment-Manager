package com.example.jackg.appointmentapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import static android.provider.BaseColumns._ID;
import static com.example.jackg.appointmentapp.Constants.TABLE_NAME;
import static com.example.jackg.appointmentapp.Constants.DETAILS;
import static com.example.jackg.appointmentapp.Constants.TITLE;
import static com.example.jackg.appointmentapp.Constants.DATE;
import static com.example.jackg.appointmentapp.Constants.TIME;


/**
 * Created by jackg on 06/04/2017.
 */

public class appointmentsActivity extends Activity {
    private appointmentsData appointments;
    boolean checkAppointment;
    String usedTitle;
    String title;
    String time;
    String details;
    String date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointments);
        appointments = new appointmentsData(this);
        Intent intent = getIntent();
        if (intent.getStringExtra("methodName").equals("addAppointment")) {
            Bundle extras = intent.getExtras();
            title = extras.getString("EXTRA_TITLE");
            System.out.println(title);
            time = extras.getString("EXTRA_TIME");                  //getting extras from intent that was passed over
            System.out.println(time);
            details = extras.getString("EXTRA_DETAILS");
            System.out.println(details);
            date = intent.getStringExtra("selectedDate");
            System.out.println(date);


            try {
                addAppointment(title, date, time, details);         //adding an appointment with method and passing parameters
            } finally {
                appointments.close();
            }
            if (checkAppointment) { //checking if the appointment title already exists on the same date
                Toast.makeText(
                        getApplicationContext(),
                        "Appointment " + "'" + usedTitle + "'" +  " already exists, please choose a different event title", Toast.LENGTH_LONG).show();  //title does exist, letting user know
                checkAppointment = false;
                onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), "Appointment saved", Toast.LENGTH_SHORT).show(); // title does not exist, letting user know that appointment was saved
                onBackPressed();
            }
        }

        if (intent.getStringExtra("methodName").equals("deleteAll")) {          // if method passed over is delete all
            date = intent.getStringExtra("selectedDate");
            deleteAllAppointment(date); //calling delete all method with parameter for the date

        }
        if (intent.getStringExtra("methodName").equals("selecteddelete")) {


        }


    }


    private void showSelectedAppointments(Cursor cursor) {
        StringBuilder builder = new StringBuilder("Appointments on the date: " + date);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String Title = cursor.getString(1);
            long Time = cursor.getLong(3);
            builder.append(id).append(". ");            //seperate method to query certain data
            builder.append(Time).append(" ");
            builder.append(Title).append("\n");


        }
        TextView appointments = (TextView) findViewById(R.id.appointments);
        appointments.setText(builder);
    }





    private void showAppointments(Cursor cursor) {
        StringBuilder builder = new StringBuilder("Saved appointments:\n");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String Title = cursor.getString(1);
            String Date = cursor.getString(2);
            long Time = cursor.getLong(3);
            String Details = cursor.getString(4);  // main method to get all data from the table
            builder.append(id).append(": ");
            builder.append(Title).append(": ");
            builder.append(Date).append(": ");
            builder.append(Time).append(": ");
            builder.append(Details).append("\n");
            if (Title.equals(title) && Date.equals(date)) {
                checkAppointment = true;
                usedTitle = Title;
            }
        }
        TextView appointments = (TextView) findViewById(R.id.appointments);
        appointments.setText(builder);

        }

    public void deleteAllAppointment(String date) {

        SQLiteDatabase db = appointments.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, "DATE" + "=?", new String[] { date }); //delete from table name the date where date equals date entered
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");     // deleting from the database where the name equals the table name.
        } finally {
            appointments.close();
        }

        Toast.makeText(getApplicationContext(), "All appointments on: " +"'" + date + "'" + " are deleted", Toast.LENGTH_SHORT).show();
        onBackPressed();                        // confirmation of deleting

    }





    public void addAppointment(String title, String date, String time, String details) {
        Cursor cursor = getAppointments();
        showAppointments(cursor);
        if (checkAppointment) {
            System.out.println("true");
        }
        else {

                            //adding an appoinment in to the table, assinging values to their column names
            SQLiteDatabase db = appointments.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TITLE, title);
            values.put(DATE, date);
            values.put(TIME, time);
            values.put(DETAILS, details);
            db.insertOrThrow(TABLE_NAME, null, values);
        }

    }

    private static String[] FROM = {_ID, TITLE, DATE, TIME, DETAILS};
    private static String ORDER_BY = _ID + " ASC";          // querying data from the database.

    private Cursor getAppointments() {
        SQLiteDatabase db = appointments.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
        startManagingCursor(cursor);
        return cursor;
    }
}


