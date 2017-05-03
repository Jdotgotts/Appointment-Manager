package com.example.jackg.appointmentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // my imports
import android.widget.Toast;
import static android.provider.BaseColumns._ID;
import static com.example.jackg.appointmentapp.Constants.DATE;
import static com.example.jackg.appointmentapp.Constants.TABLE_NAME;
import static com.example.jackg.appointmentapp.Constants.TIME;
import static com.example.jackg.appointmentapp.Constants.TITLE;

/**
 * Created by jackg on 07/04/2017.
 */

public class selectedDelete extends Activity {
    private appointmentsData selectDeleted;
    Button selectedDelete;
    EditText selectNumber;
    String number, title, time, date, selectedTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectDeleted = new appointmentsData(this);
        setContentView(R.layout.selecteddelete);
        Intent intent = getIntent();

        selectedDelete = (Button) (findViewById(R.id.deleteSelectedBtn));
        selectNumber = (EditText) (findViewById(R.id.deleteNumber));                //declaring views

        selectedDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = selectNumber.getText().toString();
                Cursor cursor = getAppointments();          //caling cursor methods
                showAppointments(cursor);


                AlertDialog.Builder newBuilder = new AlertDialog.Builder(selectedDelete.this);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                number = selectNumber.getText().toString();         //positivte or negative dialog box asking if customer really wants to delete appointment
                                deleteAppointment(number);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };
                newBuilder.setMessage("Would you like to delete the event: " + selectedTitle).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show(); //message for confirmation
            }
        });



            Bundle extras = intent.getExtras();
            title = extras.getString("EXTRA_TITLE");
            time = extras.getString("EXTRA_TIME");          //getting the variables passed over from the activity
            date = intent.getStringExtra("selectedDate");
            try {
                Cursor cursor = getAppointments();
                showAppointments(cursor);
            } finally {
                selectDeleted.close();
            }



    }


    public void deleteAppointment(String number) {

        SQLiteDatabase db = selectDeleted.getWritableDatabase();

        try {
            db.delete(TABLE_NAME, "_ID" + "=?", new String[]{number});              //deleting the row with the id number given.
        } finally {
            selectDeleted.close();
        }
        Toast.makeText(getApplicationContext(), "Deleted" + "'" +selectedTitle + "'", Toast.LENGTH_SHORT).show();
        onBackPressed();            //confirmation of deleting and back button pressed
    }

    private void showAppointments(Cursor cursor) {
        StringBuilder builder = new StringBuilder("Saved appointments for: " + date + "\n");


        while (cursor.moveToNext()) {

            long id = cursor.getLong(0);
            String Title = cursor.getString(1);         //gets data and outputs it in to text view
            long Time = cursor.getLong(2);
            builder.append(id + ".  ");
            builder.append(Time).append(":00  ");
            builder.append(Title).append("");
            builder.append("\n");

            try {
                int number2 = Integer.parseInt(number);
                if (number2 == id) {            //if entered number is same, get title
                    selectedTitle = Title;
                }
            }catch (NumberFormatException e){

            }

            TextView appointments = (TextView) findViewById(R.id.appointments);
            appointments.setText(builder);

        }

    }

    private static String[] FROM = {_ID, TITLE, TIME};
    private static String ORDER_BY = TIME + " DESC";

    private Cursor getAppointments() {
        SQLiteDatabase db = selectDeleted.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, DATE + "=?", new String[]{date}, _ID, null, ORDER_BY, null); // querying table data.
        startManagingCursor(cursor);
        return cursor;
    }

}




