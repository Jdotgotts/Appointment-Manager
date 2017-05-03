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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;


/**
 * Created by jackg on 09/04/2017.
 */

public class thesaurusList extends Activity {
    private ListView resultList;
    String thesaurusWord;
    private Handler guiThread;
    private ExecutorService thesaurusThread;            //declaring variables for threading and runnables
    private Runnable updateTask;
    private Future<?> suggPending;
    private List<String> items;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //creating the saved state
        setContentView(R.layout.thesaurus); // setting the layout from the xml file
        Intent intent = getIntent();
        thesaurusWord = intent.getStringExtra("thesaurusWord");
        System.out.println(thesaurusWord);
        initThreading();
        findViews();            //calling methods on start up
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {               //textWatch methods for suggestion

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = (String) parent.getItemAtPosition(position);             //on click listner for clicking on list
                System.out.println(query);
            }
        };

        resultList.setOnItemClickListener(clickListener);
    }
    private void initThreading() {
        guiThread = new Handler();
        System.out.println("initalising");
        thesaurusThread = Executors.newSingleThreadExecutor();              //initating the threading

        updateTask = new Runnable() {

            @Override
            public void run() {

                if (thesaurusWord.length() !=0) {



                    try {
                        thesaurusTask thesaurusTask = new thesaurusTask(thesaurusList.this, thesaurusWord);
                        System.out.println("doing it");         // insantiting and calling task
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
    }       // finding views

    private void setAdapters() {
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);       //set adapter to the data for web service
        resultList.setAdapter(adapter);
    }

    private void queueUpdate(long delayMillis){
        guiThread.removeCallbacks(updateTask);          // puts the thread in a que
        guiThread.postDelayed(updateTask, delayMillis);
    }

    public void setThesaurus(List<String> words){
        guiSetList(resultList, words);
    }       // sets the results to the result list from the GUI thread

    private void guiSetList(final ListView view, final List<String> list) {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                setList(list);
            }
        });       // sets the results to the result list from the GUI thread
    }
        private void setText(int id){
        adapter.clear();
        adapter.add(getResources().getString(id)); // set text of value of adapater
    }

    private void setList(List<String> list) {
        adapter.clear();                //sets the list from the adapter data
        adapter.addAll(list);


    }


    }



