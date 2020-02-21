package com.example.apiModels;

import com.google.gson.annotations.SerializedName;

public class ParcelApi {
    @SerializedName("id")
    private Integer id;
    @SerializedName("cultivatedArea")
    private String name;
    @SerializedName("fuelApplication")
    private String fuelApplication;
    @SerializedName("arimrOperator")
    private String arimrOperator;
}
