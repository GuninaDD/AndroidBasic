package com.geekbrains.weather_application.presenter;

import android.widget.CheckBox;

public class WthrPresenter {

    private static WthrPresenter instance = null;

    private static final Object syncObj = new Object();

    private boolean isWing;
    private boolean isPressure;

    private WthrPresenter() {
        isWing = true;
        isPressure = false;
    }

    public void setWing(CheckBox cbW) {
        isWing = cbW.isChecked();
    }

    public void setPressure(CheckBox cbP) {
        isPressure = cbP.isChecked();
    }

    public boolean getWing() {
        return isWing;
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
