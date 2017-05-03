package com.example.jackg.appointmentapp;

/**
 * Created by jackg on 06/04/2017.
 */

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
    public static final String TABLE_NAME = "Appointments";
    public static final String TITLE = "title";
    public static final String DATE = "date"; // declaring constants so I don't have to keep making them for every class
    public static final String TIME = "time";
    public static final String DETAILS = "details";
}

