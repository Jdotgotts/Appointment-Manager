package com.example.jackg.appointmentapp;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;  // my imports
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jackg on 09/04/2017.
 */

public class thesaurusTask implements Runnable {
    private static final String TAG = "thesaurusTask";
    private final thesaurusList thesaurus;          // variables
    private final String thesaurusWord;


    thesaurusTask(thesaurusList context, String thesaurusWord) {
        this.thesaurus = context;               //constructor for task class with parameters
        this.thesaurusWord = thesaurusWord;
    }

    @Override
    public void run() {
        System.out.println("IT'S RUNNING");
        try {
            List<String> thesaurusWords = doThesaurus(thesaurusWord);
            thesaurus.setThesaurus(thesaurusWords);             // runing the web service http
        } catch (IOException | InterruptedException | XmlPullParserException e) { // catches
            e.printStackTrace();
        }

    }


    private List<String> doThesaurus(String thesaurusWord) throws IOException, XmlPullParserException, InterruptedException {
        List<String> words = new LinkedList<String>();

        String error = null;
        HttpURLConnection con = null;
        Log.d(TAG, "doThesaurus(" + thesaurusWord + ")");               // doing the thesaurus task

        try {
            if (Thread.interrupted())
                throw new InterruptedException();


            String q = URLEncoder.encode(thesaurusWord, "UTF-8");       // The word variables for what word the users searches
            System.out.println(q);
            URL url = new URL("http://thesaurus.altervista.org/thesaurus/v1?word=" + q + "%20&language=en_US&key=Vli3HjhOaSxHPZ6sb7WB&output=xml"); //url for the web service
            con = (HttpURLConnection) url.openConnection(); //open connection
            con.setReadTimeout(10000 /* milliseconds */); //times out after certain mills
            con.setConnectTimeout(15000 /* milliseconds */); //times out after certain mills
            con.setRequestMethod("GET"); // sets it to a getter method
           // con.addRequestProperty("Referer", "http://www.pragprog.com/book/eband4"); // from hello android book
            con.setDoInput(true); // sets input to true usually for getter methods only
            con.connect(); // conect to web service
            if (Thread.interrupted()) throw new InterruptedException();


            XmlPullParser parser = Xml.newPullParser(); // pull parser to read xml
            parser.setInput(con.getInputStream(), null); // get stream from web service
            String name = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) { // while the tag is not at the end of the document


                if (eventType == XmlPullParser.START_TAG) {         //each start tag gets assigned to variable name per loop
                    name = parser.getName();
                }


                if (eventType == XmlPullParser.TEXT && name != null & name.equals("synonyms")) { // if it equals my tag i am looking for

                    String text = parser.getText(); // get text of the tag node
                   words.add(text); // add to arraylist



                }
                eventType = parser.next(); // next line  in parse pull
            }


            if (Thread.interrupted()) throw new InterruptedException();
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            System.out.println("IO exception");
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XmlPullParserException", e);   // catches

            System.out.println("PARSER EXCEPTION");
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException", e);

            System.out.println("INTERRUPTED");
        } finally {
            if (con != null) { // if connection is not null disconnect
                con.disconnect();
            }
        }
        if (error != null) {
            words.clear();
            words.add(error);   // clear if there is not an error
        }
        if (words.size() == 0) {
            words.add("No words found"); // if no results found
        }

        if (words.size() == 0) {
            System.out.println("No result"); // testing
        }
        Log.d(TAG, " -> returned " + words); // log for result
        return words; // return arraylist to GUI thread
}
}
