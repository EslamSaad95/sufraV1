package com.example.laptophome.sufra.data.local.RoomDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MenuItemEntity.class},version = 5,exportSchema = false)
public abstract class RoomManager extends RoomDatabase {
    private static RoomManager roomManager;
    public abstract RoomDao roomDao();
    public static synchronized RoomManager getinstance(Context context)
    {
        if(roomManager==null)
        {
            roomManager=Room.databaseBuilder(context.getApplicationContext(),RoomManager.class
            ,"Sofra Datebase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomManager;
    }

}
