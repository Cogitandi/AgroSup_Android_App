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
    @SerializedName("arimrOperator")
    private int arimrOperatorId;

    public ParcelApi(int id, String parcelNumber, int cultivatedArea, boolean fuelApplication, int arimrOperatorId) {
        this.id = id;
        this.parcelNumber = parcelNumber;
        this.cultivatedArea = cultivatedArea;
        this.fuelApplication = fuelApplication;
        this.arimrOperatorId = arimrOperatorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public int getCultivatedArea() {
        return cultivatedArea;
    }

    public void setCultivatedArea(int cultivatedArea) {
        this.cultivatedArea = cultivatedArea;
    }

    public boolean isFuelApplication() {
        return fuelApplication;
    }

    public void setFuelApplication(boolean fuelApplication) {
        this.fuelApplication = fuelApplication;
    }

    public int getArimrOperatorId() {
        return arimrOperatorId;
    }

    public void setArimrOperatorId(String arimrOperatorId) {
        if (arimrOperatorId != "null") {
            this.arimrOperatorId = new Integer(arimrOperatorId.substring(15));
        } else {
            this.arimrOperatorId = 0;
        }
    }
}
