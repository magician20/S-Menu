package com.example.magician.resturantm.ui.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.magician.resturantm.data.RestaurantRepository;

/**
 * Created by magic on 1/25/2018.
 *    this factory is used to create ViewModel
 *    @link MainActivityViewModel and connect it with repository by passing the object
 *    @link RestaurantRepository
 */

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RestaurantRepository mRepository;

    public MainViewModelFactory(RestaurantRepository repository) {
        mRepository = repository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
