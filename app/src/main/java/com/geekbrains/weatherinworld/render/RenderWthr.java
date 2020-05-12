package com.geekbrains.weatherinworld.render;

import android.annotation.SuppressLint;

import com.geekbrains.weatherinworld.adapter.Week;
import com.geekbrains.weatherinworld.rest.wthrentities.WthrRequestRestModel;
import com.geekbrains.weatherinworld.rest.wthrfivedaysentities.WthrFiveDaysRequestRestModel;

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
    private int wthrIcon;

    public void renderWeather(WthrRequestRestModel model) {
        try {
            this.wthrDscrptn = renderWthrDscrptn(model.weather[0].description);
            this.wthrWind = renderWindValue(model.wind.speed, setWind);
            this.wthrPressure = renderPressureValue(model.main.pressure, setPressure);
            this.wthrValue = renderWthrValue(model.main.temp);
            this.wthrDate = renderCurrentDate(model.dt);
            this.wthrIcon = model.weather[0].id;
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    public List<Week> renderWeatherOnFiveDays(WthrFiveDaysRequestRestModel fiveDaysModel) {
        try {
            for (int i = 0; i < 40; i++) {
                weekList.add(new Week(renderCurrentDate(fiveDaysModel.list[i].dt),
                        renderCurrentHour(fiveDaysModel.list[i].dt),
                        renderWthrValue(fiveDaysModel.list[i].main.temp),
                        renderWindValue(fiveDaysModel.list[i].wind.speed, setWind),
                        renderPressureValue(fiveDaysModel.list[i].main.pressure, setPressure),
                        renderWthrDscrptn(fiveDaysModel.list[i].weather[0].description)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return weekList;
    }

    @SuppressLint("SetTextI18n")
    private String  renderWthrValue(float temp){
        return String.format(Locale.getDefault(), "%.2f",
                temp) + "\u2103";
    }

    private String renderWindValue(float wind, boolean setWind){
        if (setWind) {
            return wind + " m/s";
        } else {
            return emptyText;
        }
    }

    private String renderPressureValue(float pressure, boolean setPressure){
        if (setPressure) {
            return pressure + " hPa";
        } else {
            return emptyText;
        }
    }

    private String renderCurrentDate(long dt){
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd", Locale.getDefault());
        return dateFormat.format(new Date(dt * 1000));
    }

    private String renderCurrentHour(long dt){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return dateFormat.format(new Date(dt * 1000));
    }

    private String renderWthrDscrptn(String description){
        return description.toUpperCase();
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

    public int getWthrIcon() {
        return wthrIcon;
    }
}
