package com.example.magician.resturantm.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.magician.resturantm.AppExecutors;
import com.example.magician.resturantm.data.database.ItemDao;
import com.example.magician.resturantm.data.database.ItemEntry;
import com.example.magician.resturantm.data.network.RestaurantNetworkDataSource;

import java.util.List;


/**
 * Handles data operations in Sunshine. Acts as a mediator between {@link }
 * and {@link }
 */
public class RestaurantRepository {
    private static final String LOG_TAG = RestaurantRepository.class.getSimpleName();

    private boolean mInitialized=false;//used to check if service used one time at app live
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RestaurantRepository sInstance;

    private final ItemDao mItemDao;
    private final RestaurantNetworkDataSource mRestaurantNetworkDataSource;
    private final AppExecutors mExecutors;

    private RestaurantRepository
            (ItemDao itemDao, RestaurantNetworkDataSource NetworkDataSource, AppExecutors executors) {
        mItemDao = itemDao;
        mRestaurantNetworkDataSource = NetworkDataSource;
        mExecutors = executors;
        /* get data to insert in DB*/
        LiveData<List<ItemEntry>> menuItems = mRestaurantNetworkDataSource.getCurrentDownloadedMenuItems();
        //  observe the data (menuItems) change
        menuItems.observeForever(itemEntries -> {
            mExecutors.diskIO().execute(() -> {
                //insert the data in DB
                mItemDao.bulkInsert(itemEntries);
                Log.d(LOG_TAG, "New values inserted");
            });//thread
        });

    }

    public synchronized static RestaurantRepository getsInstance
            (ItemDao itemDao,RestaurantNetworkDataSource NetworkDataSource, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RestaurantRepository(itemDao, NetworkDataSource, executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

/* ---------------------------------------------------------------------------------------------- */
                                   /* Define When Service is Started  */
/* ---------------------------------------------------------------------------------------------- */
    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     * >>used  with getData() methods(because if data from server mean it will be exhibited for changing)
     */
    private void initializeData() {
        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if(mInitialized)return;
        mInitialized = true;
       ////we can made check here inside the thread to add more control on the service
        mExecutors.diskIO().execute(this::startFetchMenuItemsService);

    }
    /**
     * Network related operation (Start the service that get the data from server)
     */
    private void startFetchMenuItemsService() {
        mRestaurantNetworkDataSource.startFetchItemsMenuService();
    }
/* ---------------------------------------------------------------------------------------------- */
                                       /* DAO Methods */
/* ---------------------------------------------------------------------------------------------- */
    /**
     *  get items data from Database and hold it inside LiveData
     */
    public LiveData<List<ItemEntry>> getItemsMenu(){
        initializeData();
        return mItemDao.getItemsMenu();
    }

    /**
     *  delete item from data from Database
     */
    public void deleteItem(ItemEntry itemEntry) {
        mItemDao.deleteItem(itemEntry);

    }

}