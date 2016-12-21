package com.example.android.weather;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Vector;

/**
 * Created by kashyap on 21/12/16.
 */

public class FetchWeather  {

    private void getWeatherDataFromJson(String forecastJsonstr , String locationSetting) throws JSONException{

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String OWM_CITY = "city";
        final String OWM_CITY_NAME = "name";
        final String OWM_COORD = "coord";

        // Location coordinate
        final String OWM_LATITUDE = "lat";
        final String OWM_LONGITUDE = "lon";

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "list";

        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WINDSPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";

        // All temperatures are children of the "temp" object.
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_WEATHER_ID = "id";

        try {
            JSONObject forecastJson =new JSONObject(forecastJsonstr);
            JSONArray weatherArray =new JSONArray(OWM_LIST);

            JSONObject cityJson =new JSONObject(OWM_CITY);
            String cityNmae =cityJson.getString(OWM_CITY_NAME);

            JSONObject cityCord =cityJson.getJSONObject(OWM_COORD);

            double cityLatitude =cityCord.getDouble(OWM_LATITUDE);
            double cityLongitude=cityCord.getDouble(OWM_LONGITUDE);

            long locationId =addLocation(locationSetting,cityNmae,cityLatitude,cityLongitude);

            Vector<ContentValues> cVVector =new Vector<>(weatherArray.length());

            android.text.format.Time dayTime = new android.text.format.Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = android.text.format.Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new android.text.format.Time();

            for(int i = 0; i < weatherArray.length(); i++) {
                // These are the values that will be collected.
                long dateTime;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                double high;
                double low;

                String description;
                int weatherId;

                JSONObject dayForecast =weatherArray.getJSONObject(i);

                dateTime =dayTime.setJulianDay(julianStartDay+i);
                pressure =dayForecast.getDouble(OWM_PRESSURE);
                humidity =dayForecast.getInt(OWM_HUMIDITY);
                windSpeed =dayForecast.optDouble(OWM_WINDSPEED);
                windDirection=dayForecast.getDouble(OWM_WIND_DIRECTION);

                JSONObject weatherObject =dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description =weatherObject.getString(OWM_DESCRIPTION);
                weatherId =weatherObject.getInt(OWM_WEATHER_ID);

                JSONObject tempratureObject =dayForecast.getJSONObject(OWM_TEMPERATURE);

                high =tempratureObject.getDouble(OWM_MAX);
                low =tempratureObject.getDouble(OWM_MIN);

                ContentValues weatherValues =new ContentValues();

                weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationId);
                weatherValues.put(WeatherEntry.COLUMN_DATE, dateTime);
                weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, humidity);
                weatherValues.put(WeatherEntry.COLUMN_PRESSURE, pressure);
                weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
                weatherValues.put(WeatherEntry.COLUMN_DEGREES, windDirection);
                weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, high);
                weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, low);
                weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, description);
                weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, weatherId);

                cVVector.add(weatherValues);
            }
            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

            }


    }
}
