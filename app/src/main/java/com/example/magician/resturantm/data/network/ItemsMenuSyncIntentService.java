package com.example.magician.resturantm.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.magician.resturantm.utilities.InjectorUtils;

/**
 * Created by magic on 1/30/2018.
 * run in the background
 */

/**
 * An {@link IntentService} subclass for immediately scheduling a sync with the server off of the
 * main thread.
 */
public class ItemsMenuSyncIntentService extends IntentService {
    private static final String LOG_TAG = ItemsMenuSyncIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * ItemsMenuSyncIntentService Used to name the worker thread, important only for debugging.
     */
    public ItemsMenuSyncIntentService() {
        super("ItemsMenuSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Intent service started");
        // using injection to connect with NetworkDataSource & call fetch method
        RestaurantNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchMenuItems();

    }
}
