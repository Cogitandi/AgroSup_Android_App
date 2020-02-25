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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlant() {
        if(plant != null) {
            return plant.getPlantName();
        } else {
            return "brak";
        }

    }
}
