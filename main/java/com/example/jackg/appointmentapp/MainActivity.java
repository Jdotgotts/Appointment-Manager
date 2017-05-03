package com.example.jackg.appointmentapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button createApp, saveApp, editApp, deleteApp, moveApp, searchApp, thesaurusBtn, searchBtn;
    EditText detailsTxt, titleTxt, timeTxt, thesaurusTxt, thesaurusTxt2;
    final Context context = this;
    String date;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //creating the saved state
        setContentView(R.layout.activity_main); // setting the layout from the xml file
        final CalendarView myCalendar = (CalendarView) findViewById(R.id.myCalendar); // finding the object
        myCalendar.setMinDate(1463918226920L); //setting the min date to may 2016 in milliseconds
        date = String.valueOf(myCalendar.getDate());
        myCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1; // as the month index starts at 0, add one to get real month number.
                date = String.valueOf(dayOfMonth + "/" + month  + "/" + year);  //getting selected date
                System.out.println(date);
            }
        });

        createApp = (Button) findViewById(R.id.createBtn);
        editApp = (Button) findViewById(R.id.viewEditBtn);
        deleteApp = (Button) findViewById(R.id.delAppointmentBtn); // declaring button views
        moveApp = (Button) findViewById(R.id.moveAppointmentBtn);
        searchApp = (Button) findViewById(R.id.searchBtn);

        createApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context); //declaring dialog box and assigning it to this context
                dialog.setContentView(R.layout.create_appointment); //finding layout and assinging dialog box to it
                dialog.setTitle("Book an appointment"); // setting title of dialog box


                dialog.show(); // showing dialog box
                Window window = dialog.getWindow();
                window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                detailsTxt = (EditText) dialog.findViewById(R.id.detailsTxt);
                titleTxt = (EditText) dialog.findViewById(R.id.titleTxt); // declaring views
                timeTxt = (EditText) dialog.findViewById(R.id.timeTxt);
                saveApp = (Button) dialog.findViewById(R.id.saveBtn);
                thesaurusBtn = (Button) dialog.findViewById(R.id.thesaurusBtn);
                thesaurusTxt = (EditText) dialog.findViewById(R.id.thesaurusTxt);
                saveApp.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String title = titleTxt.getText().toString();
                        String time = timeTxt.getText().toString(); // setting listener for save button
                        String details = detailsTxt.getText().toString();
                        if (title.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Error: Title can not be empty", Toast.LENGTH_SHORT).show(); // if title is empty
                        } else {
                            Intent intent = new Intent(MainActivity.this, appointmentsActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("EXTRA_TITLE", title);
                            extras.putString("EXTRA_TIME", time);
                            extras.putString("EXTRA_DETAILS", details);   // post extra to intent for creation of appointment
                            intent.putExtras(extras);
                            intent.putExtra("methodName", "addAppointment");
                            intent.putExtra("selectedDate", date);
                            MainActivity.this.startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });
                thesaurusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String thesaurusString =  thesaurusTxt.getText().toString();
                        Intent intent = new Intent(MainActivity.this, thesaurusList.class); // thesaurus button listener
                        intent.putExtra("thesaurusWord", thesaurusString); // passing word for thesaurus
                        MainActivity.this.startActivity(intent);
                    }
                });


            }

        });



        deleteApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder newBuilder = new AlertDialog.Builder(MainActivity.this);
                newBuilder.setTitle(R.string.delete_title); // delete button listener
                newBuilder.setItems(new CharSequence[]
                        {"Delete all appointments for that date", "Select appointment to delete"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(MainActivity.this, appointmentsActivity.class);
                                intent.putExtra("methodName", "deleteAll");
                                intent.putExtra("selectedDate", date);     //delete all appointments from selected date
                                MainActivity.this.startActivity(intent);
                                break;
                            case 1:
                                Intent intent2 = new Intent(MainActivity.this, selectedDelete.class);
                                intent2.putExtra("selectedDate", date);
                                intent2.putExtra("methodName", "selecteddelete"); //let user choose what they want deleted
                                MainActivity.this.startActivity(intent2);           //opens new activity
                                break;
                        }

                    }
                });
                mDialog = newBuilder.show(); // shows dialog

            }
        });

        editApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, editAppointment.class);
                intent.putExtra("selectedDate", date);      //edit button listenenr
                MainActivity.this.startActivity(intent);
            }
        });

        moveApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, selectedMove.class);
                intent.putExtra("selectedDate", date);
                MainActivity.this.startActivity(intent);   //move button listener

            }
        });

        searchApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context); //declaring dialog box and assigning it to this context
                dialog.setContentView(R.layout.search_appointment); //finding layout and assinging dialog box to it
                dialog.setTitle("Search an appointment"); // setting title of dialog box
                dialog.show(); // showing dialog box
                Window window = dialog.getWindow();
                window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
               searchBtn = (Button) dialog.findViewById(R.id.searchBtn);
                titleTxt = (EditText) dialog.findViewById(R.id.titleTxt);
                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, searchActivity.class);

                        String title = titleTxt.getText().toString();           //search button listener
                        intent.putExtra("title", title);
                        MainActivity.this.startActivity(intent);

                    }
                });
            }
        });


    }


}
