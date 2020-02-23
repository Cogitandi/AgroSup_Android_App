package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Field {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "yearPlanId")
    private int yearPlanId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearPlanId() {
        return yearPlanId;
    }

    public void setYearPlanId(int yearPlanId) {
        this.yearPlanId = yearPlanId;
    }

    public Field(int id, String name, int yearPlanId) {
        this.id = id;
        this.name = name;
        this.yearPlanId = yearPlanId;
    }
}
