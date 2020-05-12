package com.geekbrains.weatherinworld.rest.wthrfivedaysentities;

import com.geekbrains.weatherinworld.rest.wthrentities.WthrRequestRestModel;
import com.google.gson.annotations.SerializedName;

public class WthrFiveDaysRequestRestModel {
    @SerializedName("list") public WthrRequestRestModel[] list;

}
