package com.example.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.entities.Field;
import com.example.entities.YearPlan;

import java.util.List;

public class YearPlanWithFields {
    @Embedded
    public YearPlan yearPlan;
    @Relation(
            parentColumn = "id",
            entityColumn = "yearPlanId"
    )
    public List<Field> fields;
}
