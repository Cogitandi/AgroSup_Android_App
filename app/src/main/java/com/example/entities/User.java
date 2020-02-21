package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int uid;

    public User(int uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    @ColumnInfo(name = "email")
    public String email;

}