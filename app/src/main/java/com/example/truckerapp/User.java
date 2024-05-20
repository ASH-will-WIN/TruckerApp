package com.example.truckerapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String phoneNumber;
    public String password;
    public double latitude;
    public double longitude;

    public User(String name, String phoneNumber, String password, double latitude, double longitude) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
