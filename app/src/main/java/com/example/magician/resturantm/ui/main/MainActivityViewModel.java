package com.example.magician.resturantm.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.magician.resturantm.data.RestaurantRepository;
import com.example.magician.resturantm.data.database.ItemEntry;

import java.util.List;

/**
 * Created by magic on 1/30/2018.
 *  @link MainActivityViewModel   Hold data until Activity is destroyed
 */

public class MainActivityViewModel extends ViewModel {
    private final LiveData<List<ItemEntry>> mMenuItems;
    private final RestaurantRepository mRepository;

    MainActivityViewModel(RestaurantRepository repository) {
        mRepository = repository;
        mMenuItems= mRepository.getItemsMenu();
    }

    public LiveData<List<ItemEntry>> getMenuItems() {
        return mMenuItems;
    }

    //here we can cancel Volley request by using Repository
}
