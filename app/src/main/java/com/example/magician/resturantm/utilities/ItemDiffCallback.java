package com.example.magician.resturantm.utilities;

import android.support.v7.util.DiffUtil;

import com.example.magician.resturantm.data.database.ItemEntry;

import java.util.List;

/**
 * Created by Magician on 1/30/2018.
 * this class can be used to compare.(i want to build one that take any type and .class of the type and generate
 *                           the code of compare)
 */

/**
 *  Otherwise we use DiffUtil to calculate the changes and update accordingly. This
 *  shows the four methods you need to override to return a DiffUtil callback. The
 *  old list is the current list stored in mForecast, where the new list is the new
 *  values passed in from the observing the database.
 * */
public class ItemDiffCallback extends DiffUtil.Callback {
    private final List<ItemEntry> mOldItem;
    private final List<ItemEntry> mNewItem;


    public ItemDiffCallback(List<ItemEntry> OldItem, List<ItemEntry> NewItem) {
        this.mOldItem = OldItem;
        this.mNewItem = NewItem;
    }

    @Override
    public int getOldListSize() {
        return mOldItem.size();
    }

    @Override
    public int getNewListSize() {
        return mNewItem.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItem.get(oldItemPosition).getId() == mNewItem.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ItemEntry oldItem = mOldItem.get(oldItemPosition);
        ItemEntry newItem = mNewItem.get(newItemPosition);
        return oldItem.getId() == newItem.getId()
                && oldItem.getName().equalsIgnoreCase(newItem.getName())
                && oldItem.getDescription().equalsIgnoreCase(newItem.getDescription())
                && oldItem.getPrice() == newItem.getPrice()
                && oldItem.getThumbnail().equals(newItem.getThumbnail());
    }
}
