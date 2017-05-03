package com.example.jackg.appointmentapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;


/**
 * Created by jackg on 09/04/2017.
 */

public class thesaurusChooseList extends Activity {
    private ListView resultList;
    String thesaurusWord, ID;
    private Handler guiThread;
    private ExecutorService thesaurusThread;
    private Runnable updateTask;
    private Future<?> suggPending;
    private List<String> items;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //creating the saved state
        setContentView(R.layout.thesaurus); // setting the layout from the xml file
        Intent intent = getIntent();
        thesaurusWord = intent.getStringExtra("thesaurusWord");
        ID= intent.getStringExtra("ID");
        System.out.println(thesaurusWord);
        initThreading();
        findViews();
        setListeners();
        setAdapters();
        queueUpdate(1000 /* milliseconds */);

    }

    private void setListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = (String) parent.getItemAtPosition(position);
                System.out.println(query);
                Intent intent = new Intent(thesaurusChooseList.this, editAppointment.class);
                intent.putExtra("thesaurusWord", query);
                intent.putExtra("ID", ID);                          //seperate class for thesaurus two method
                intent.putExtra("methodName", "replace detail");
                thesaurusChooseList.this.startActivity(intent);
            }
        };

        resultList.setOnItemClickListener(clickListener);
    }
    private void initThreading() {
        guiThread = new Handler();
        System.out.println("initalising");
        thesaurusThread = Executors.newSingleThreadExecutor();

        updateTask = new Runnable() {

            @Override
            public void run() {

                if (thesaurusWord.length() !=0) {



                    try {
                        thesaurusChooseTask thesaurusTask = new thesaurusChooseTask(thesaurusChooseList.this, thesaurusWord);
                        System.out.println("doing it");
                        suggPending = thesaurusThread.submit(thesaurusTask);
                    } catch (RejectedExecutionException e) {
                        System.out.println("REJECTED");
                    }
                }
            }
        };
    }

    private void findViews() {
        resultList = (ListView) findViewById(R.id.resultList);
    }

    private void setAdapters() {
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        resultList.setAdapter(adapter);
    }

    private void queueUpdate(long delayMillis){
        guiThread.removeCallbacks(updateTask);
        guiThread.postDelayed(updateTask, delayMillis);
    }

    public void setThesaurus(List<String> words){
        guiSetList(resultList, words);
    }

    private void guiSetList(final ListView view, final List<String> list) {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                setList(list);
            }
        });
    }
        private void setText(int id){
        adapter.clear();
        adapter.add(getResources().getString(id));
    }

    private void setList(List<String> list) {
        adapter.clear();
        adapter.addAll(list);


    }


    }



