package com.example.magician.resturantm.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * Created by Magician on 1/29/2018.
 * DB  //use ,exportSchema =false to ride of warning //glide 4.6 doesn't support last ach update
 */
@Database(entities = {ItemEntry.class}, version = 1,exportSchema =false  )
public abstract class RestaurantDatabase extends RoomDatabase {
    private static final String LOG_TAG = RestaurantDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "restaurant.db";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile RestaurantDatabase sInstance;

    /**
     * this method to create singleton object from DB
     *
     * @param context is used to get applicationContext
     **/
    public static RestaurantDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder
                        (context.getApplicationContext(),RestaurantDatabase.class,RestaurantDatabase.DATABASE_NAME).build();
                Log.d(LOG_TAG, "Create New DB Object");
            }
        }
        return sInstance;
    }


    public abstract ItemDao itemDao();//Getters for Dao
}
