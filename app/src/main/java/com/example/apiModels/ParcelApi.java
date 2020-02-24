package com.example.apiModels;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class ParcelApi {
    @SerializedName("id")
    private int id;
    @SerializedName("parcelNumber")
    private String parcelNumber;
    @SerializedName("cultivatedArea")
    private int cultivatedArea;
    @SerializedName("fuelApplication")
    private boolean fuelApplication;
    @SerializedName("ArimrOperator")
    private String arimrOperatorId;
    @SerializedName("field")
    private String fieldId;

    public int getId() {
        return id;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public int getCultivatedArea() {
        return cultivatedArea;
    }

    public boolean isFuelApplication() {
        return fuelApplication;
    }

    public String getArimrOperatorId() {
        return arimrOperatorId;
    }

    public String getFieldId() {
        return fieldId;
    }
}
