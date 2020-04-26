package com.geekbrains.weatherinworld.rest.wthrentities;

import com.google.gson.annotations.SerializedName;

public class WthrRequestRestModel {
    @SerializedName("weather") public WeatherRestModel[] weather;
    @SerializedName("main") public MainRestModel main;
    @SerializedName("wind") public WindRestModel wind;
    @SerializedName("dt") public long dt;
    @SerializedName("name") public String name;
    //@SerializedName("cod") public int cod;
}
