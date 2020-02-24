package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.entities.YearPlan;

import java.util.List;

@Dao
public interface YearPlanDao {
    @Query("SELECT * FROM yearPlan")
    List<YearPlan> getAll();

    @Query("SELECT * FROM yearplan WHERE userId = :userId")
    List<YearPlan> loadAllByUser(int userId);

    @Query("DELETE FROM yearplan WHERE userId = :userId")
    void deleteYearPlansByUserId(int userId);

    @Query("SELECT * FROM yearplan WHERE id = :yearPlanId")
    YearPlan getYearPlanById(int yearPlanId);

    @Insert
    void insertAll(YearPlan... yearPlan);

    @Delete
    void delete(YearPlan yearPlan);

    @Transaction
    @Query("SELECT * FROM yearPlan where userId IN (:userId) AND id = (:yearPlanId)")
    YearPlanWithOperators yearPlanWithOperators(int userId, int yearPlanId);

    @Transaction
    @Query("SELECT * FROM yearPlan where userId IN (:userId) AND id = (:yearPlanId)")
    YearPlanWithFields yearPlanWithFields(int userId, int yearPlanId);

    @Transaction
    @Query("SELECT * FROM yearPlan where userId IN (:userId) AND id = (:yearPlanId)")
    YearPlanWithParcels yearPlanWithParcels(int userId, int yearPlanId);

    @Transaction
    @Query("SELECT * FROM field where yearPlanId = (:yearPlanId)")
    List<FieldWithParcels> fieldsWithParcels(int yearPlanId);
}
