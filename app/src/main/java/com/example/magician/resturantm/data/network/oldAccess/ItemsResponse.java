package com.example.magician.resturantm.data.network.oldAccess;

import com.example.magician.resturantm.data.database.ItemEntry;

import java.util.List;

/**
 * Created by Magician on 1/30/2018.
 *  Hold items come from json object
 */

public class ItemsResponse {
    private final List<ItemEntry> mItems;

    public ItemsResponse(final List<ItemEntry> items) {//
        mItems = items;

    }

    public List<ItemEntry> getItems() {//
        return mItems;
    }
}
