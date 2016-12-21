package com.example.android.weather;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by kashyap on 21/12/16.
 */

public class Utility {

    public static String getPreferredLocation(Context context){

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("location","110091");

    }

    public static boolean isMetric(Context context){

        SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("units","metric").equals("metric");
    }

    static String formatTemprature(double temprature,boolean isMetric) {
        double temp;
        if (!isMetric ) {
            temp = 9 * temprature / 5 + 32;
        } else {
            temp = temprature;
        }

        return String.format("%.0f", temp);
    }

    static String formatDate(long dateInMillis)
    {
        Date date =new Date(dateInMillis);
        return DateFormat.getDateInstance().format(date);
    }

}
