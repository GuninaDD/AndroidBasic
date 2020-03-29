package com.geekbrains.weather_application.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class WeatherGenerator {
    public WeatherGenerator() {
    }

    public String getWeatherValue() {
        final Random random = new Random();
        int value = random.nextInt(40) + 1;
        if ((value % 2) == 0) {
            return "+" + value;
        } else {
            return "-" + value;
        }
    }

    public String getWing(Boolean wingEnabled) {
        final Random random = new Random();
        if (wingEnabled) {
            return (random.nextInt(200) + 1) + " m/s";
        } else {
            return "";
        }
    }

    public String getDate() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM hh:mm", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public String getPressure(Boolean pressureEnabled) {
        final Random random = new Random();
        if (pressureEnabled) {
            return (random.nextInt(760) + 1) + " mmHg";
        } else {
            return "";
        }
    }

    public String getDscrptn(String[] dscrptns) {
        final Random random = new Random();
        return dscrptns[random.nextInt(4)];
    }
}
