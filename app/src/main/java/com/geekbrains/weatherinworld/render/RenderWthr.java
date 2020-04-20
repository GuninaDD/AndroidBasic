package com.geekbrains.weatherinworld.render;

import android.annotation.SuppressLint;

import com.geekbrains.weatherinworld.adapter.Week;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RenderWthr {

    public RenderWthr(boolean setWind, boolean setPressure) {
        this.setWind = setWind;
        this.setPressure = setPressure;
        this.weekList = new ArrayList<>();
    }

    private String emptyText = "";
    private boolean setWind;
    private boolean setPressure;
    private List<Week> weekList;
    private String wthrDscrptn;
    private String wthrWind;
    private String wthrPressure;
    private String wthrValue;
    private String wthrDate;



    public void renderWeather(JSONObject jsonObject) {
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject wind = jsonObject.getJSONObject("wind");

            this.wthrDscrptn = renderWthrDscrptn(details);
            this.wthrWind = renderWindValue(wind, setWind);
            this.wthrPressure = renderPressureValue(main, setPressure);
            this.wthrValue = renderWthrValue(main);
            this.wthrDate = renderCurrentDate(jsonObject);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public List<Week> renderWeatherOnFiveDays(JSONObject jsonObject) {
        try {
            for (int i = 0; i < 40; i++) {
                JSONObject list = jsonObject.getJSONArray("list").getJSONObject(i);
                JSONObject details = list.getJSONArray("weather").getJSONObject(0);
                JSONObject main = list.getJSONObject("main");
                JSONObject wind = list.getJSONObject("wind");

                weekList.add(new Week(renderCurrentDate(list), renderCurrentHour(list),
                        renderWthrValue(main), renderWindValue(wind, setWind),
                        renderPressureValue(main, setPressure),
                        renderWthrDscrptn(details)));
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return weekList;
    }

    @SuppressLint("SetTextI18n")
    private String  renderWthrValue(JSONObject main) throws JSONException {
        return String.format(Locale.getDefault(), "%.2f",
                main.getDouble("temp")) + "\u2103";
    }

    private String renderWindValue(JSONObject wind, boolean setWind) throws JSONException {
        if (setWind) {
            return wind.getString("speed") + " m/s";
        } else {
            return emptyText;
        }
    }

    private String renderPressureValue(JSONObject main, boolean setPressure) throws JSONException {
        if (setPressure) {
            return main.getString("pressure") + " hPa";
        } else {
            return emptyText;
        }
    }

    private String renderCurrentDate(JSONObject list) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd", Locale.getDefault());
        return dateFormat.format(new Date(list.getLong("dt") * 1000));
    }

    private String renderCurrentHour(JSONObject list) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return dateFormat.format(new Date(list.getLong("dt") * 1000));
    }

    private String renderWthrDscrptn(JSONObject details) throws JSONException {
        return details.getString("description").toUpperCase();
    }

    public String getCurrentWthrDscrptn() {
        return wthrDscrptn;
    }

    public String getCurrentWthrWind() {
        return wthrWind;
    }

    public String getCurrentWthrPressure() {
        return wthrPressure;
    }

    public String getCurrentWthrValue() {
        return wthrValue;
    }

    public String getCurrentWthrDate() {
        return wthrDate;
    }
}
