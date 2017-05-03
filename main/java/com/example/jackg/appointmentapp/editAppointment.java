package com.example.jackg.appointmentapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import static android.provider.BaseColumns._ID;
import static com.example.jackg.appointmentapp.Constants.DATE;
import static com.example.jackg.appointmentapp.Constants.DETAILS;
import static com.example.jackg.appointmentapp.Constants.TABLE_NAME;
import static com.example.jackg.appointmentapp.Constants.TIME;
import static com.example.jackg.appointmentapp.Constants.TITLE;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class editAppointment extends Activity {
    String ID, date, thesaurusWord;
    Button editBtn, saveApp, thesaurusBtn;
    String title, time, details;
    private appointmentsData editAppointment;       // declaring variables
    EditText detailsTxt, titleTxt, timeTxt, selectNumber, thesaurusTxt;
    final Context context = this;



    protected void onCreate(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(context);      //getting contect
        super.onCreate(savedInstanceState);
        editAppointment = new appointmentsData(this);       //setting table to data
        setContentView(R.layout.edit_appointment);              //on create method
        Intent intent = getIntent();
        date = intent.getStringExtra("selectedDate");
        editBtn = (Button) (findViewById(R.id.deleteEditBtn));
        Cursor cursor = getAppointments();          // creating cursor object with method
        showAppointments(cursor); // assigning cursor to method
        selectNumber = (EditText) (findViewById(R.id.deleteNumber));  // declaring edit text
try {
    if (intent.getStringExtra("methodName").equals("replace detail")) {
        thesaurusWord = intent.getStringExtra("thesaurusWord");
        String id = intent.getStringExtra("ID");                // trying to change value of detail from thesaurus word
        selectNumber.setText(id);
        editBtn.performClick();
        detailsTxt.setText(thesaurusWord);

    }
}catch(NullPointerException e){

}





        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.edit_dialog); //finding layout and assinging dialog box to it
                dialog.setTitle("Edit an appointment"); // setting title of dialog box
                dialog.show(); // showing dialog box
                Window window = dialog.getWindow();         // dialog dimensions
                window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                ID = selectNumber.getText().toString();
                saveApp = (Button) dialog.findViewById(R.id.saveBtn);
                thesaurusBtn = (Button) dialog.findViewById(R.id.thesaurusBtn);                 // edit button opens up dialog with data already filled in
                thesaurusTxt = (EditText) dialog.findViewById(R.id.thesaurusTxt);
                detailsTxt = (EditText) dialog.findViewById(R.id.detailsTxt);
                detailsTxt.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        int start = detailsTxt.getSelectionStart();
                        int end = detailsTxt.getSelectionEnd();
                        if(start == -1 && end == -1){           //trying to get highlighted word
                            return true;
                        }
                        String mSelectedText=((EditText)v).getText().toString().substring(start, end);
                        System.out.println(mSelectedText);

                        return false;
                    }
                });
                thesaurusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // int startSelection=  detailsTxt.getSelectionStart();
                      //  int endSelection= detailsTxt.getSelectionEnd();
                      //  String thesaurusString = thesaurusTxt.getText().toString().substring(startSelection, endSelection);
                      //  System.out.println(thesaurusString);
                       String thesaurusString = detailsTxt.getText().toString().substring(0,4);
                        Intent intent = new Intent(editAppointment.this, thesaurusChooseList.class);                //passing word for thesaurus service
                        intent.putExtra("thesaurusWord", thesaurusString);
                        intent.putExtra("ID", ID);
                        editAppointment.this.startActivity(intent);



                    }
                });


                saveApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (title.equals("")) {
                            Toast.makeText(getApplicationContext(), "Error: Title can not be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            title = titleTxt.getText().toString();
                            System.out.println(title);
                            time = timeTxt.getText().toString();                    //saving/updating the table and sending the values
                            details = detailsTxt.getText().toString();
                            addAppointment(title,time,details);
                            Toast.makeText(getApplicationContext(), "Appointment updated", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                    }
                });


            System.out.println("ID: " + ID);

                    Cursor cursor = getSelectedAppointment();
                    showSelectedAppointments(cursor);
                    titleTxt = (EditText) dialog.findViewById(R.id.titleTxt);
                    timeTxt = (EditText) dialog.findViewById(R.id.timeTxt);                 // filling data
                    detailsTxt = (EditText) dialog.findViewById(R.id.detailsTxt);
                    titleTxt.setText(title);
                    timeTxt.setText(time);
                    detailsTxt.setText(details);


            }
        });




    }



    public void addAppointment(String title, String time, String details) {
        Cursor cursor = getSelectedAppointment();
        showSelectedAppointments(cursor);
        SQLiteDatabase db = editAppointment.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, title);                               //adding new values in to table e.g updating them
        values.put(TIME, time);
        values.put(DETAILS, details);
        db.update(TABLE_NAME, values, "_ID" + "=" + ID, null);

    }



    private void showAppointments(Cursor cursor) {
        StringBuilder builder = new StringBuilder("Appointments on the date: " + date + "\n");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String Title = cursor.getString(1);
            long Time = cursor.getLong(2);                  //showing table and getting values in cursor object
            String Details = cursor.getString(3);
            builder.append(id).append(". ");
            builder.append(Title).append(" ");
            builder.append(Time).append(" ");
            builder.append(Details).append("\n");


        }
        TextView appointments = (TextView) findViewById(R.id.appointments);
        appointments.setText(builder);              //outputting cursor object from stringbuilder in to textview

    }

    private void showSelectedAppointments(Cursor cursor) {

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String Title = cursor.getString(1);
            title = cursor.getString(1);
            long Time = cursor.getLong(2);              // getting columns for fill data
            time = cursor.getString(2);
            String Details = cursor.getString(3);
            details = cursor.getString(3);

        }


    }

    private static String[] FROM = {_ID, TITLE, TIME, DETAILS};
    private static String ORDER_BY = _ID + " ASC ";

    private Cursor getAppointments() {
        SQLiteDatabase db = editAppointment.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, DATE + "=?", new String[]{date}, null, null, ORDER_BY);      //querying table
        startManagingCursor(cursor);
        return cursor;
    }


    private Cursor getSelectedAppointment() {
        SQLiteDatabase db = editAppointment.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, _ID + "=?", new String[]{ID}, null, null, ORDER_BY);     //querying table for data fill
        startManagingCursor(cursor);
        return cursor;
    }


}
