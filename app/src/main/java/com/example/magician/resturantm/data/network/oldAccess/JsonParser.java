package com.example.magician.resturantm.data.network.oldAccess;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.magician.resturantm.data.database.ItemEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Magician on 1/30/2018.
 */

public final class JsonParser {
    private static final String LOG_TAG = JsonParser.class.getSimpleName();
    private static final String ITEM_ID = "id";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_PRICE = "price";
    private static final String ITEM_THUMBNAIL = "thumbnail";


    /**
     * This method parses JSON from a web response and returns an array of Strings
     *
     * @param itemsJsonStr JSON response from server
     * @return Array of Strings describing items data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @NonNull
    public static ItemsResponse parse(final String itemsJsonStr) throws JSONException {
        ItemEntry[] items = fromJson(itemsJsonStr);
        Log.d(LOG_TAG, "JSON parse end.");
        return new ItemsResponse(Arrays.asList(items));//
    }

    private static ItemEntry[] fromJson(final String itemsJsonStr) throws JSONException {
        JSONArray itemsJsonArray = new JSONArray(itemsJsonStr);
        ItemEntry[] items = new ItemEntry[itemsJsonArray.length()];

        for (int i = 0; i < itemsJsonArray.length(); i++) {
            // Get the JSON object representing the item
            JSONObject item = itemsJsonArray.getJSONObject(i);
            int id = item.getInt(ITEM_ID);
            String name = item.getString(ITEM_NAME);
            String description = item.getString(ITEM_DESCRIPTION);
            double price = item.getDouble(ITEM_PRICE);
            String thumbnail = item.getString(ITEM_THUMBNAIL);
            ItemEntry itemEntry = new ItemEntry(id, name, description, price, thumbnail);
            items[i] = itemEntry;
        }

        return items;
    }

}
