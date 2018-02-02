package com.example.magician.resturantm.data.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Magician on 2/1/2018.
 */

public class NetworkSingleton {
    private static NetworkSingleton sInstance;
    private static Context mContext;

    private RequestQueue mRequestQueue;
    //private ImageLoader mImageLoader;//not used

    private NetworkSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
//        //not used
//        mImageLoader = new ImageLoader(mRequestQueue,
//                new ImageLoader.ImageCache() {
//                    private final LruCache<String, Bitmap> cache = new LruCache<>(20);
//
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return cache.get(url);
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//                        cache.put(url, bitmap);
//                    }
//                });


    }

    /*
    here argument is context not appContext
    so i have to use .getApplicationContext() when i call this method
    */
    public synchronized static NetworkSingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkSingleton(context);
        }
        return sInstance;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

//    public ImageLoader getImageLoader() {
//        return mImageLoader;
//    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
