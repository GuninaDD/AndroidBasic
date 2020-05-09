package com.geekbrains.weatherinworld.adapter;

import java.io.Serializable;

public class Week implements Serializable {
    private String day;
    private String hour;
    private String value;
    private String wind;
    private String pressure;
    private String dscrptn;

    public Week(String day, String hour, String value, String wind, String pressure, String dscrptn) {
        this.day = day;
        this.hour = hour;
        this.value = value;
        this.wind = wind;
        this.pressure = pressure;
        this.dscrptn = dscrptn;
    }

    String getDay() {
        return day;
    }

    String getHour() {
        return hour;
    }

    String getValue() {
        return value;
    }

    String getWind() {
        return wind;
    }

    String getPressure() {
        return pressure;
    }

    String getDscrptn() {
        return dscrptn;
    }
}
