package com.rednineteen.android.sampleapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.rednineteen.android.sampleapp.net.OkHttpStack;

/**
 * Created on 5/02/2018 by Juan Velasquez - email:  juan@rednineteen.com.
 */

public class App extends Application {

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Init Volley RequestQueue. Use a custom HTTP Stack to allow for http to https redirects
        requestQueue = Volley.newRequestQueue(this, new OkHttpStack());
        // Init volley image loader.
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        });

    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static synchronized App getInstance() {
        return instance;
    }

}
