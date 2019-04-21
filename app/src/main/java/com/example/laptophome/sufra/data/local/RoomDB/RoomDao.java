package com.example.laptophome.sufra.data.local.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RoomDao {

    @Insert
    void Add(MenuItemEntity ... item);

    @Update
     void Update(MenuItemEntity ... item);

    @Delete
    void Delete(MenuItemEntity ... item);

    //to make query to select all
    //mmking aktob ay query
    @Query("Select * from MenuItemEntity")
    List<MenuItemEntity> GetAll();


    @Query("Delete  from MenuItemEntity")
    void DeleteAll();
}
