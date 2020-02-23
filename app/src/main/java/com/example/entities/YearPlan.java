package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class YearPlan {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "startYear")
    private int startYear;

    @ColumnInfo(name = "endYear")
    private int endYear;

    @ColumnInfo(name = "userId")
    private int userId;

    public int getId() {
        return id;
    }

    public YearPlan(int id, int startYear, int endYear, int userId) {
        this.id = id;
        this.startYear = startYear;
        this.endYear = endYear;
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
