package com.example.apiModels;

import com.google.gson.annotations.SerializedName;

public class Operator {
    @SerializedName("id")
    private Integer id;
    @SerializedName("firstName")
    private String firstName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @SerializedName("surname")
    private String surname;
}
