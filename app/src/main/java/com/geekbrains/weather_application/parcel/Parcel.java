package com.geekbrains.weather_application.parcel;

import java.io.Serializable;

public class Parcel implements Serializable {
    private String cityName;
    private String wthrValue;
    private String wing;
    private String pressure;
    private String currentDate;
    private String wthrDescription;

    public String getCityName() {
        return cityName;
    }

    public String getWthrValue() {
        return wthrValue;
    }

    public String getWing() {
        return wing;
    }

    public String getPressure() {
        return pressure;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getWthrDescription() {
        return wthrDescription;
    }

    public Parcel(String cityName, String wthrValue, String wing, String pressure, String currentDate, String wthrDescription) {
        this.cityName = cityName;
        this.wthrValue = wthrValue;
        this.wing = wing;
        this.pressure = pressure;
        this.currentDate = currentDate;
        this.wthrDescription = wthrDescription;
    }
}
