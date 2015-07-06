package com.novoda.pxfetcher;

import android.graphics.Bitmap;

import com.novoda.pxfetcher.CacheManager;
import com.novoda.pxfetcher.task.TagWrapper;

public class CacheBitmapProcessor implements BitmapProcessor {

    private final CacheManager cacheManager;

    public CacheBitmapProcessor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Bitmap elaborate(TagWrapper tagWrapper, Bitmap bitmap) {
        if (tagWrapper.isNoLongerValid()) {
            return null;
        }

        if (bitmap == null) {
            return bitmap;
        }

        cacheManager.put(tagWrapper.getSourceUrl(), bitmap);
        return bitmap;
    }

}
