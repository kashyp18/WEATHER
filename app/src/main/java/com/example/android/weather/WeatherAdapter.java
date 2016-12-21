package com.example.android.weather;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;

/**
 * Created by kashyap on 21/12/16.
 */

/**@link ForecastAdapter} exposes a list of weather forecast
 *
 */

public class WeatherAdapter extends CursorAdapter {

    WeatherAdapter(Context context, Cursor c, int flags){ super(context,c,flags);}
    /** Prepare for weather high/lows for presentation
     */

    private String formatHighLows(double high,double low)
    {
        boolean isMetric =Utility.isMetric(mContext);
        String highLowstr =Utility.formatTemprature(high, isMetric) +"/"+Utility.formatTemprature(low ,isMetric);
        return highLowstr;
    }
}
