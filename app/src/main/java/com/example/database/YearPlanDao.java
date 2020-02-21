package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Parcel;
import com.example.entities.User;
import com.example.entities.YearPlan;

import java.util.List;

@Dao
public interface YearPlanDao {
    @Query("SELECT * FROM yearPlan")
    List<YearPlan> getAll();

    @Query("SELECT * FROM yearplan WHERE user = :user")
    List<YearPlan> loadAllByUser(User user);

    @Insert
    void insertAll(YearPlan... yearPlan);

    @Delete
    void delete(YearPlan yearPlan);
}
