package com.example.apiModels;

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
    private String arimrOperator;
    @SerializedName("field")
    private String field;
    @SerializedName("yearPlan")
    private String yearPlan;

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

    public String getArimrOperator() {
        return arimrOperator;
    }

    public String getField() {
        return field;
    }

    public String getYearPlan() {
        return yearPlan;
    }
}
