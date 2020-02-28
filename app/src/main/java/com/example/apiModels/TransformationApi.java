package com.example.apiModels;

import com.google.gson.annotations.SerializedName;

public class TransformationApi {
    @SerializedName("x")
    private String x;
    @SerializedName("y")
    private String y;
    @SerializedName("z")
    private String z;

    public String getCoordinates() {
        return y + "," + x + "," + y + "," + x;
    }
}
