package com.example.jackg.appointmentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import static com.example.jackg.appointmentapp.Constants.DATE;
import static com.example.jackg.appointmentapp.Constants.DETAILS;
import static com.example.jackg.appointmentapp.Constants.TABLE_NAME;
import static com.example.jackg.appointmentapp.Constants.TIME;
import static com.example.jackg.appointmentapp.Constants.TITLE;

/**
 * Created by jackg on 09/04/2017.
 */

public class moveAppointment extends Activity {
    private appointmentsData moveSelectedAppointment;
    String date, newDate, title, ID;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); //creating the saved state
        setContentView(R.layout.move_appointment2); // setting the layout from the xml file
        CalendarView myCalendar = (CalendarView) findViewById(R.id.myCalendar); // finding the object
        myCalendar.setMinDate(1463918226920L); //setting the min date to may 2016 in milliseconds
        moveSelectedAppointment = new appointmentsData(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        title = extras.getString("title");                  //new acitvity for user to choose date they want to move appointment
        ID = extras.getString("ID");
        date = intent.getStringExtra("selectedDate");

        myCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1; // as the month index starts at 0, add one to get real month number.
                newDate = String.valueOf(dayOfMonth + "/" + month + "/" + year);        //getting selected date
                System.out.println(date);
                AlertDialog.Builder newBuilder = new AlertDialog.Builder(moveAppointment.this);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                    moveSelectedAppointment();
                                Toast.makeText(getApplicationContext(), "Appointment moved to: " + newDate, Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };
                newBuilder.setMessage("Are you sure you want to move the event: " + "'" + title + "'" + " to the date: " + newDate).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();               //confirmation of moving event
            }


        });




            }

    private void moveSelectedAppointment() {
        SQLiteDatabase db = moveSelectedAppointment.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, newDate);
        db.update(TABLE_NAME, values, "_ID" + "=" + ID, null);
    }

    }

