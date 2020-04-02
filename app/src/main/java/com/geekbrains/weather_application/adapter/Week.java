package com.geekbrains.weather_application.adapter;

import java.io.Serializable;

public class Week implements Serializable {
    private String day;

    public Week(String day, String value, String wing, String pressure) {
        this.day = day;
        this.value = value;
        this.wing = wing;
        this.pressure = pressure;
    }

    private String value;
    private String wing;
    private String pressure;

    String getDay() {
        return day;
    }

    String getValue() {
        return value;
    }

    String getWing() {
        return wing;
    }

    String getPressure() {
        return pressure;
    }
}
