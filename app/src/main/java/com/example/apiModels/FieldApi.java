package com.example.apiModels;

import com.google.gson.annotations.SerializedName;

public class FieldApi {

    public class Plant {
        @SerializedName("name")
        private String plantName;

        public String getPlantName() {
            return plantName;
        }
    }

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("plant")
    private Plant plant;
    @SerializedName("plantVariety")
    private String plantVariety;
    @SerializedName("yearPlan")
    private String yearPlan;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlant() {
        if (plant != null) {
            return plant.getPlantName();
        } else {
            return "brak";
        }

    }

    public String getPlantVariety() {
        return plantVariety;
    }

    public String getYearPlan() {
        return yearPlan;
    }
}
