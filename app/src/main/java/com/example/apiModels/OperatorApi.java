package com.example.apiModels;

import com.google.gson.annotations.SerializedName;

public class OperatorApi {
    @SerializedName("id")
    private Integer id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("surname")
    private String surname;
    @SerializedName("yearPlan")
    private String yearPlan;

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getYearPlan() {
        return yearPlan;
    }
}
