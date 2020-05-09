package com.geekbrains.weatherinworld.parcel;

import java.io.Serializable;


public class Parcel implements Serializable {
    private String cityName;
    private boolean setWind;
    private boolean setPressure;

    public boolean isSetWind() {
        return setWind;
    }

    public boolean isSetPressure() {
        return setPressure;
    }

    public Parcel () {
        this.cityName = "";
    }

    public Parcel(String cityName, boolean setWind, boolean setPressure) {
        this.cityName = cityName;
        this.setWind = setWind;
        this.setPressure = setPressure;
    }

    public String getCityName() {
        return cityName;
    }

}
