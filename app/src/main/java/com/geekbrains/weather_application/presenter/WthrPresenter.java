package com.geekbrains.weather_application.presenter;

import android.widget.CheckBox;

public class WthrPresenter {

    private static WthrPresenter instance = null;

    private static final Object syncObj = new Object();

    private boolean isWind;
    private boolean isPressure;

    private WthrPresenter() {
        isWind = true;
        isPressure = false;
    }

    public void setWind(CheckBox cbW) {
        isWind = cbW.isChecked();
    }

    public void setPressure(CheckBox cbP) {
        isPressure = cbP.isChecked();
    }

    public boolean getWind() {
        return isWind;
    }

    public boolean getPressure() {
        return isPressure;
    }

    public static WthrPresenter getInstance() {
        synchronized (syncObj) {
            if (instance == null) {
                instance = new WthrPresenter();
            }
            return instance;
        }
    }

}
