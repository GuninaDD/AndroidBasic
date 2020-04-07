package com.geekbrains.weather_application.loader;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherDataLoader {
    private static final String OPEN_WEATHER_API_KEY = "59c805f9388a14ad6a047bbcc92e49c1";
    private static final String OPEN_WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String OPEN_WEATHER_API_URL_ON_FIVE_DAYS =
            "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";
    private static final String KEY = "x-api-key";
    private static URL url;

    public static JSONObject getJSONData(String city, int apiIndex) {
        try {
            initURL(apiIndex, city);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, OPEN_WEATHER_API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;

            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append("\n");
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt("cod") != 200) {
                return null;
            } else {
                return jsonObject;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private static void initURL(int apiIndex, String city) throws MalformedURLException {
        switch (apiIndex) {
            case 1:
                url = new URL(String.format(OPEN_WEATHER_API_URL, city));
                break;
            case 2:
                url = new URL(String.format(OPEN_WEATHER_API_URL_ON_FIVE_DAYS, city));
        }
    }

    //public static JSONObject getJSONDataOnFiveDays(String city) {
    //    try {
    //        URL url = new URL(String.format(OPEN_WEATHER_API_URL_ON_FIVE_DAYS, city));
    //        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //        connection.addRequestProperty(KEY, OPEN_WEATHER_API_KEY);
//
    //        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    //        StringBuilder rawData = new StringBuilder(1024);
    //        String tempVariable;
//
    //        while ((tempVariable = reader.readLine()) != null) {
    //            rawData.append(tempVariable).append("\n");
    //        }
//
    //        reader.close();
//
    //        JSONObject jsonObject = new JSONObject(rawData.toString());
    //        if(jsonObject.getInt("cod") != 200) {
    //            return null;
    //        } else {
    //            return jsonObject;
    //        }
    //    } catch (Exception exc) {
    //        exc.printStackTrace();
    //        return null;
    //    }
    //}
}
