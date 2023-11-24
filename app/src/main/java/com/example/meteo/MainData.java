// MainData.java
package com.example.meteo;

import com.google.gson.annotations.SerializedName;

public class MainData {
    @SerializedName("temp")
    private double temp;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("temp_min")
    private double temp_min;
    @SerializedName("temp_max")
    private double temp_max;
    @SerializedName("pressure")
    private int pressure;
    @SerializedName("feels_like")
    private double feels_like;

    public double getTemp() {
        return temp;
    }
    public int getHumidity() {
        return humidity;
    }
    public double getTemp_min() {
        return temp_min;
    }
    public double getTemp_max() { return temp_max;}
    public int getPressure() {
        return pressure;
    }
    public double getFeels_like() { return feels_like; }

}
