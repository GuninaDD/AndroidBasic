package com.geekbrains.weatherinworld.rest;

import com.geekbrains.weatherinworld.rest.wthrentities.WthrRequestRestModel;
import com.geekbrains.weatherinworld.rest.wthrfivedaysentities.WthrFiveDaysRequestRestModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWthr {
    @GET("data/2.5/weather")
    Call<WthrRequestRestModel> loadWeather(@Query("q") String city,
                                           @Query("appid") String keyApi,
                                           @Query("units") String units);

    @GET("data/2.5/forecast")
        Call<WthrFiveDaysRequestRestModel> loadFiveDaysWeather (@Query("q") String city,
                                                                @Query("appid") String keyApi,
                                                                @Query("units") String units);

    }
