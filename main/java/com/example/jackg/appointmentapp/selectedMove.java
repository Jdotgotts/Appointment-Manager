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
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.example.jackg.appointmentapp.Constants.DATE;
import static com.example.jackg.appointmentapp.Constants.DETAILS;
import static com.example.jackg.appointmentapp.Constants.TABLE_NAME;
import static com.example.jackg.appointmentapp.Constants.TIME;
import static com.example.jackg.appointmentapp.Constants.TITLE;

/**
 * Created by jackg on 09/04/2017.
 */

public class selectedMove extends Activity {
    private appointmentsData moveAppointment;
    Button selectedMove;
    EditText selectNumberMove;
    String date, selectedTitle, number;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveAppointment = new appointmentsData(this); // creating a new from the same data
        setContentView(R.layout.move_appointment);
        Intent intent = getIntent();
        date = intent.getStringExtra("selectedDate");   // on creation getting variables, declaring views and calling cursor methods to populate textview with table data.
        selectedMove = (Button) (findViewById(R.id.moveBtn));
        selectNumberMove = (EditText) (findViewById(R.id.moveNumber));
        Cursor cursor = getAppointments();
        showAppointments(cursor);

        selectedMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = selectNumberMove.getText().toString();
                Cursor cursor = getAppointments();
                showAppointments(cursor);


                    AlertDialog.Builder newBuilder = new AlertDialog.Builder(selectedMove.this);

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    Intent intent = new Intent(selectedMove.this, moveAppointment.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("title", selectedTitle);
                                    extras.putString("ID", number);
                                    extras.putString("selectedDate", date); // setting dialog box to ask user if they are certain they want to move appointment
                                    intent.putExtras(extras);
                                    selectedMove.this.startActivity(intent);
                                    break;


                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };
                    newBuilder.setMessage("Would you like to move the event: " + selectedTitle).setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show(); // confirmation message asking user if they want to moveappointment

                }

        });
    }




    private void showAppointments(Cursor cursor) {
        StringBuilder builder = new StringBuilder("Appointments on the date: " + date + "\n");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String Title = cursor.getString(1);
            long Time = cursor.getLong(2);
            String Details = cursor.getString(3); // getting data from table and assinging it to variables
            builder.append(id).append(". ");
            builder.append(Title).append(" ");
            builder.append(Time).append(" ");
            builder.append(Details).append("\n");

            try {
                int number2 = Integer.parseInt(number);
                if (number2 == id) {
                    selectedTitle = Title; // if user entered number equals id number, get title
                }
                else {
                    selectedTitle = "";
                }
            }catch (NumberFormatException e){

            }


        }
        TextView appointments = (TextView) findViewById(R.id.appointments);
        appointments.setText(builder);

    }

    private static String[] FROM = {_ID, TITLE, TIME, DETAILS};
    private static String ORDER_BY = _ID + " ASC ";

    private Cursor getAppointments() {
        SQLiteDatabase db = moveAppointment.getReadableDatabase(); // getting data from table and querying it
        Cursor cursor = db.query(TABLE_NAME, FROM, DATE + "=?", new String[]{date}, null, null, ORDER_BY);
        startManagingCursor(cursor);
        return cursor;
    }
}
