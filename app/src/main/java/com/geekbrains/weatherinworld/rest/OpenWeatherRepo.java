package com.geekbrains.weatherinworld.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherRepo {
    private static OpenWeatherRepo singleton = null;
    private IOpenWthr API;

    private OpenWeatherRepo() {
        API = createAdapter();
    }

    public static OpenWeatherRepo getSingleton() {
        if(singleton == null) {
            singleton = new OpenWeatherRepo();
        }

        return singleton;
    }

    public IOpenWthr getAPI() {
        return API;
    }

    private IOpenWthr createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(IOpenWthr.class);
    }
}
