package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    User findByName(String email);

    @Query("SELECT * FROM user WHERE logged = (:tru) LIMIT 1")
    User findLogged(boolean tru);

    @Transaction
    @Query("SELECT * FROM user where id IN (:userId)")
    UserWithYearPlans userWithYearPlans(int userId);

    @Update
    void updateUsers(User... users);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}