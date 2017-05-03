package com.example.jackg.appointmentapp;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jackg on 09/04/2017.
 */

public class thesaurusChooseTask implements Runnable {
    private static final String TAG = "thesaurusTask";
    private final thesaurusChooseList thesaurus;
    private final String thesaurusWord;


    thesaurusChooseTask(thesaurusChooseList context, String thesaurusWord) {
        this.thesaurus = context;
        this.thesaurusWord = thesaurusWord;
    }

    @Override
    public void run() {
        System.out.println("IT'S RUNNING");
        try {
            List<String> thesaurusWords = doThesaurus(thesaurusWord);
            thesaurus.setThesaurus(thesaurusWords);
        } catch (IOException | InterruptedException | XmlPullParserException e) {
            e.printStackTrace();
        }

    }


    private List<String> doThesaurus(String thesaurusWord) throws IOException, XmlPullParserException, InterruptedException {
        List<String> words = new LinkedList<String>();

        String error = null;
        HttpURLConnection con = null;
        Log.d(TAG, "doThesaurus(" + thesaurusWord + ")");

        try {
            if (Thread.interrupted())
                throw new InterruptedException();


            String q = URLEncoder.encode(thesaurusWord, "UTF-8");
            System.out.println(q);
            URL url = new URL("http://thesaurus.altervista.org/thesaurus/v1?word=" + q + "%20&language=en_US&key=Vli3HjhOaSxHPZ6sb7WB&output=xml");
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setRequestMethod("GET");
            con.addRequestProperty("Referer", "http://www.pragprog.com/book/eband4");
            con.setDoInput(true);
            con.connect();
            if (Thread.interrupted()) throw new InterruptedException();


            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(con.getInputStream(), null);
            String name = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {


                if (eventType == XmlPullParser.START_TAG) {
                    name = parser.getName();
                }


                if (eventType == XmlPullParser.TEXT && name != null & name.equals("synonyms")) {

                    String text = parser.getText();
                   words.add(text);



                }
                eventType = parser.next();
            }


            if (Thread.interrupted()) throw new InterruptedException();
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            System.out.println("IO exception");
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XmlPullParserException", e);

            System.out.println("PARSER EXCEPTION");
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException", e);

            System.out.println("INTERRUPTED");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        if (error != null) {
            words.clear();
            words.add(error);
        }
        if (words.size() == 0) {
            words.add("No words found");
        }

        if (words.size() == 0) {
            System.out.println("No result");
        }
        Log.d(TAG, " -> returned " + words);
        return words;
}
}
