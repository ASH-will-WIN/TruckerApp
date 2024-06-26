package com.example.truckerapp;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber AND password = :password LIMIT 1")
    User login(String phoneNumber, String password);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber")
    User findByPhoneNumber(String phoneNumber);
}
