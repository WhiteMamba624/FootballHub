package com.gligamihai.footballhub.Utils;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public float windSpeed;

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }
}
