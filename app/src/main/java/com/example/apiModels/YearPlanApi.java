package com.example.apiModels;

import com.google.gson.annotations.SerializedName;

public class YearPlanApi {

    @SerializedName("id")
    private Integer id;
    @SerializedName("startYear")
    private Integer startYear;
    @SerializedName("endYear")
    private Integer endYear;

    public Integer getId() {
        return id;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }
}
