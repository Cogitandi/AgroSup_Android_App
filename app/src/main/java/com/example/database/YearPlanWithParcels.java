package com.example.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.entities.Parcel;
import com.example.entities.YearPlan;

import java.util.List;

public class YearPlanWithParcels {
    @Embedded
    public YearPlan yearPlan;
    @Relation(
            parentColumn = "id",
            entityColumn = "yearPlanId"
    )
    public List<Parcel> parcels;
}
