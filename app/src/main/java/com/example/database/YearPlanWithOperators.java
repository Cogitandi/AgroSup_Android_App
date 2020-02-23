package com.example.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.entities.Operator;
import com.example.entities.YearPlan;

import java.util.List;

public class YearPlanWithOperators {
    @Embedded
    public YearPlan yearPlan;
    @Relation(
            parentColumn = "id",
            entityColumn = "yearPlanId"
    )
    public List<Operator> operators;
}
