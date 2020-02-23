package com.example.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.entities.User;
import com.example.entities.YearPlan;

import java.util.List;

public class UserWithYearPlans {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public List<YearPlan> yearPlans;
}
