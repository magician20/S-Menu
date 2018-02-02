package com.example.magician.resturantm.utilities;

import android.content.Context;
import android.util.Log;

import com.example.magician.resturantm.AppExecutors;
import com.example.magician.resturantm.data.RestaurantRepository;
import com.example.magician.resturantm.data.database.RestaurantDatabase;
import com.example.magician.resturantm.data.network.RestaurantNetworkDataSource;
import com.example.magician.resturantm.ui.main.MainViewModelFactory;

/**
 * Created by Magician on 1/30/2018.
 * Injection
 */

public class InjectorUtils {
    private static final String LOG_TAG = InjectorUtils.class.getSimpleName();

    public static RestaurantRepository provideRepository(Context context) {
        Log.d(LOG_TAG, "provideRepository");
        RestaurantDatabase database = RestaurantDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        RestaurantNetworkDataSource networkDataSource = RestaurantNetworkDataSource
                .getsInstance(context.getApplicationContext(), executors);

        return RestaurantRepository.getsInstance(database.itemDao(), networkDataSource, executors);//error here
    }

    public static RestaurantNetworkDataSource provideNetworkDataSource(Context context) {
        Log.d(LOG_TAG, "provideNetworkDataSource");
        AppExecutors executors = AppExecutors.getInstance();
        return RestaurantNetworkDataSource.getsInstance(context.getApplicationContext(), executors);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        Log.d(LOG_TAG, "provideMainActivityViewModelFactory");
        RestaurantRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

}
