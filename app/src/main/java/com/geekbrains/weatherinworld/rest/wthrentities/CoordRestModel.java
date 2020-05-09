package com.geekbrains.weatherinworld.rest.wthrentities;

import com.google.gson.annotations.SerializedName;

class CoordRestModel {
    @SerializedName("lon") public float lon;
    @SerializedName("lat") public float lat;
}
