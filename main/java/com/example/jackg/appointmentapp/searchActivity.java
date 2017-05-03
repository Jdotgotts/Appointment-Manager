package com.example.jackg.appointmentapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.jackg.appointmentapp.Constants.DATE;
import static com.example.jackg.appointmentapp.Constants.DETAILS;       //my imports
import static com.example.jackg.appointmentapp.Constants.TABLE_NAME;
import static com.example.jackg.appointmentapp.Constants.TIME;
import static com.example.jackg.appointmentapp.Constants.TITLE;

/**
 * Created by jackg on 10/04/2017.
 */

public class searchActivity extends Activity {
    String title, ID, titleResult, detailsResult, timeResult;
    EditText detailsTxt, titleTxt, timeTxt, searchNumber;
    Button searchBtn;
    private appointmentsData Searchappointment;     // declaring new table
    final Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(context);
        super.onCreate(savedInstanceState); //creating the saved state
        setContentView(R.layout.search_result); // setting the layout from the xml file
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        System.out.println(title);                          // on create method
        Searchappointment = new appointmentsData(this);
        searchNumber = (EditText) (findViewById(R.id.searchNumber));
        searchBtn = (Button) (findViewById(R.id.searchSelectedBtn));
        Cursor cursor = getAppointments();
        showAppointments(cursor);




        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = searchNumber.getText().toString();
                dialog.setContentView(R.layout.edit_dialog); //finding layout and assinging dialog box to it
                dialog.setTitle("Edit an appointment"); // setting title of dialog box
                dialog.show(); // showing dialog box
                Window window = dialog.getWindow();
                window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                Cursor cursor = getSelectedAppointment();
                showSelectedAppointments(cursor);
                titleTxt = (EditText) dialog.findViewById(R.id.titleTxt);                   //search btn listener
                timeTxt = (EditText) dialog.findViewById(R.id.timeTxt);                     //declaring views and using dialog box
                detailsTxt = (EditText) dialog.findViewById(R.id.detailsTxt);
                titleTxt.setText(titleResult);
                timeTxt.setText(timeResult);
                detailsTxt.setText(detailsResult);

            }
        });
    }

    ArrayList<String> titleArr = new ArrayList();
    ArrayList<String> detailsArr = new ArrayList();     // declaring arraylists
    ArrayList<String> IDArr = new ArrayList();

    private void showAppointments(Cursor cursor) {
        StringBuilder builder = new StringBuilder("Appointments containing the word: " + title + "\n");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String Title = cursor.getString(1);
            long Time = cursor.getLong(2);
            String Details = cursor.getString(3);
            IDArr.add(cursor.getString(0));
            titleArr.add(cursor.getString(1));          //adding data to arraylists
            detailsArr.add(cursor.getString(3));
            // builder.append(id).append(": ");
            //  builder.append(Title).append(": ");
            //  builder.append(Time).append(": ");
            //  builder.append(Details).append("\n");
            System.out.println(titleArr);
            System.out.println(detailsArr);


        }
        for (int i = 0; i < titleArr.size(); i++) {
            if (title.equalsIgnoreCase(titleArr.get(i))) {
                builder.append(IDArr.get(i) + ": ");
                builder.append(titleArr.get(i) + ": ");
                builder.append(detailsArr.get(i));
                builder.append("\n");                           //checking if appointments contain string user entered and putting any title or details that equalled the string

            } else {
                if (title.equalsIgnoreCase(detailsArr.get(i))) {
                    builder.append(IDArr.get(i) + ": ");
                    builder.append(titleArr.get(i) + ": ");
                    builder.append(detailsArr.get(i));
                    builder.append("\n");

                }


                }
            }


        TextView appointments = (TextView) findViewById(R.id.appointments);
        appointments.setText(builder);
    }


    private void showSelectedAppointments(Cursor cursor) {

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String Title = cursor.getString(1);
            titleResult = cursor.getString(1);          //getting data from table and putting it in to variables
            long Time = cursor.getLong(2);
            timeResult = cursor.getString(2);
            String Details = cursor.getString(3);
            detailsResult = cursor.getString(3);

        }


    }

    private Cursor getSelectedAppointment() {
        SQLiteDatabase db = Searchappointment.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, _ID + "=?", new String[]{ID}, null, null, ORDER_BY);
        startManagingCursor(cursor);            // querying database
        return cursor;
    }



        private static String[] FROM = {_ID, TITLE, TIME, DETAILS};
        private static String ORDER_BY = _ID + " ASC";

    private Cursor getAppointments() {
        SQLiteDatabase db = Searchappointment.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
        startManagingCursor(cursor);  // querying database
        return cursor;
    }
}
