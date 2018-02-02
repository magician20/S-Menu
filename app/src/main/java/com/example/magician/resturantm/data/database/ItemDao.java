package com.example.magician.resturantm.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by Magician on 1/29/2018.
 * queries
 *
 */

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<ItemEntry> itemEntries);//List<ItemEntry> item

    @Query("SELECT * FROM items")
    LiveData<List<ItemEntry>> getItemsMenu();//update the return to wrapped LiveData

    @Delete
    void deleteItem(ItemEntry item);


}
