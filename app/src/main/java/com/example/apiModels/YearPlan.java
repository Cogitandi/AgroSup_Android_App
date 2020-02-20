package com.example.apiModels;

import com.google.gson.annotations.SerializedName;

public class YearPlan {

    @SerializedName("id")
    private Integer id;

    @SerializedName("startYear")
    private Integer startYear;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    @SerializedName("endYear")
    private Integer endYear;
}
