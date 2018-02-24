package com.example.magician.resturantm.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.magician.resturantm.AppExecutors;
import com.example.magician.resturantm.data.database.ItemEntry;
import com.example.magician.resturantm.data.network.oldAccess.ItemsResponse;
import com.example.magician.resturantm.data.network.oldAccess.JsonParser;
import com.example.magician.resturantm.data.network.oldAccess.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by magic on 1/30/2018.
 * control data come from network
 */

public class RestaurantNetworkDataSource {
    // url to fetch menu json
    static final String sURL = "https://api.androidhive.info/json/menu.json";
    //created to store data holt inside LiveData
    private final MutableLiveData<List<ItemEntry>> mDownloadedMenuItems;//
    private static final String LOG_TAG = RestaurantNetworkDataSource.class.getSimpleName();
    private final Context mContext;
    private final AppExecutors mExecutors;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile RestaurantNetworkDataSource sInstance;

    private RestaurantNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedMenuItems = new MutableLiveData<>();
    }

    public static RestaurantNetworkDataSource getsInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RestaurantNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }
/* ---------------------------------------------------------------------------------------------- */
                                    /* get Data */
/* ---------------------------------------------------------------------------------------------- */

    /**
     * @return MutableLiveData<List<ItemEntry>> that we need to show it(remember it will be observed).
     **/
    public MutableLiveData<List<ItemEntry>> getCurrentDownloadedMenuItems() {//
        Log.d(LOG_TAG, "getCurrentDownloadedMenuItems");
        return mDownloadedMenuItems;
    }
/* ---------------------------------------------------------------------------------------------- */
                                 /* Start Service */
/* ---------------------------------------------------------------------------------------------- */

    /**
     * Starts an intent service to fetch the weather.
     */
    public void startFetchItemsMenuService() {
        Intent intentToFetch = new Intent(mContext, ItemsMenuSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.i(LOG_TAG, "Service created");
    }

    /* get data from server (this Method run inside the server) */
    void fetchMenuItems() {
        Log.d(LOG_TAG, "Fetch Menu started");
        //old way //oldPrepareMenuItems()

       /* should run this method on main thread ??*/
        //mExecutors.networkIO().execute(this::prepareMenuItems);work?yes
        prepareMenuItems();//work?yes better

    }


/* ---------------------------------------------------------------------------------------------- */
                                  /* Old Way to get data from NetWork code */
/* ---------------------------------------------------------------------------------------------- */
    private void oldPrepareMenuItems() {
        mExecutors.networkIO().execute(() -> {
            try {
                //getUrl method will return the sURL that we need to get the data
                // Use the URL to retrieve the JSON
                String jsonStringResponse = NetworkUtils.
                        getResponseFromHttpUrl(NetworkUtils.CreateURL(NetworkUtils.sURL));

                if (jsonStringResponse == null) {
                    Log.i(LOG_TAG, "JSON String is Null");
                    return;
                }
                // Parse the JSON into a list of weather forecasts
                ItemsResponse response = JsonParser.parse(jsonStringResponse);
                Log.i(LOG_TAG, "JSON Parsing finished");

                if (response != null && response.getItems().size() != 0) {//
                    Log.v(LOG_TAG, "JSON not null and has " + response.getItems().size() + " values");
                    Log.v(LOG_TAG, "First value" + response.toString());

                    // Will eventually do something with the downloaded data
                    mDownloadedMenuItems.postValue(response.getItems());//
                }

            } catch (IOException e) {
                // Server probably invalid
                Log.e(LOG_TAG, "Error IOException");
            } catch (JSONException e) {
                //mean error in fetching jsonString
                Log.e(LOG_TAG, "Error JSONException");
            }
        });
    }

    /* ---------------------------------------------------------------------------------------------- */
                                  /* Volley code */
/* ---------------------------------------------------------------------------------------------- */
    //method make volley network call and parses json and pass Value to mutableLiveData
    private void prepareMenuItems() { //error here
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, sURL, null,
                response -> {
                    if (response == null) {
                        Log.v(LOG_TAG, "Couldn't fetch the menu! null");
                        Toast.makeText(mContext.getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    /*
                     Ways :1-use loop and ItemEntry.class
                            2-use new TypeToken<List<ItemEntry>>(){}.getType();
                    */
                    //error start here because no assign happen from thumbnail to imageUrl
                    // because I used different names so it will pass null
                    List<ItemEntry> itemsMenu = new Gson().fromJson(response.toString(), new TypeToken<List<ItemEntry>>() {
                    }.getType());
                    // adding && refreshing recycler view
                    // Will eventually do something with the downloaded data
                    mDownloadedMenuItems.postValue(itemsMenu);//

                }, error -> {
            // error in getting json
            Log.d(LOG_TAG, "Error: " + error.getMessage());
            Toast.makeText(mContext.getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        // Access the RequestQueue through your singleton class.
        NetworkSingleton.getInstance(mContext).addToRequestQueue(request);
    }

}
