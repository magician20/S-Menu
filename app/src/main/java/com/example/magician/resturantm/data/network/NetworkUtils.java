package com.example.magician.resturantm.data.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Magician on 1/30/2018.
 */

final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    // url to fetch menu json
    static final String sURL = "https://api.androidhive.info/json/menu.json";

    //@return sURL
    static URL CreateURL(String url) {
        URL Url = null;
        try {
            Url = new URL(url);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException: " + e.getMessage());
        }
        return Url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The sURL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.d(LOG_TAG, "getResponseFromHttpUrl run.");
        String response = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        try {
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
               //read data from stream
                Log.d(LOG_TAG, "read data from stream.");
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();

                if (hasInput) {
                    response = scanner.next();
                }
                scanner.close();

            } else {
                Log.e(LOG_TAG, "URL Error " + urlConnection.getResponseCode());
            }
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }

    //Read data from stream
    private String ReadFromStream(InputStream in){
        String response=null;

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");
        boolean hasInput = scanner.hasNext();
        if (hasInput) {
             response = scanner.next();
        }
        scanner.close();

        return response;
    }
}
