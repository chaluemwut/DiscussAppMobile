package com.novoda.pxfetcher;

import android.graphics.Bitmap;

/**
 * Interface for all the in memory cache managers.
 */
public interface CacheManager {

    Bitmap get(String url, int width, int height);

    void put(String url, Bitmap bmp);

    void remove(String url);

    void clean();

}