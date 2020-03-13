package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.entities.Field;

import java.util.List;

@Dao
public interface FieldDao {
    @Query("SELECT * FROM field")
    List<Field> getAll();

    @Query("SELECT * FROM field WHERE yearPlanId = :yearPlanId")
    List<Field> loadAllByYearPlan(int yearPlanId);

    @Query("SELECT * FROM field WHERE id = :id")
    Field getFieldById(int id);

    @Query("DELETE FROM field WHERE yearPlanId = :yearPlanId")
    void deleteFieldsByYearPlanId(int yearPlanId);

    @Transaction
    @Query("SELECT * FROM field where id = (:fieldId)" )
    FieldWithParcels fieldsWithParcels(int fieldId);

    @Insert
    void insertAll(Field... field);

    @Delete
    void delete(Field field);


}
