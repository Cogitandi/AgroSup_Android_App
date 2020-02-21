package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Operator;
import com.example.entities.User;
import com.example.entities.YearPlan;

import java.util.List;

@Dao
public interface OperatorDao {
    @Query("SELECT * FROM operator")
    List<Operator> getAll();

    @Query("SELECT * FROM operator WHERE yearPlan = :yearPlan")
    List<Operator> loadAllByYearPlan(YearPlan yearPlan);

    @Insert
    void insertAll(Operator... operator);

    @Delete
    void delete(Operator operator);
}
