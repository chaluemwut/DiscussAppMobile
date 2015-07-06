package com.novoda.pxfetcher;

import android.graphics.Bitmap;

import com.novoda.pxfetcher.CacheManager;
import com.novoda.pxfetcher.task.Result;
import com.novoda.pxfetcher.task.Retriever;
import com.novoda.pxfetcher.task.TagWrapper;

public class MemoryRetriever<T extends TagWrapper<V>, V> implements Retriever<T,V> {

    private static final int IGNORED = 0;
    private final CacheManager cacheManager;
    private final BitmapProcessor bitmapProcessor;

    public MemoryRetriever(CacheManager cacheManager, BitmapProcessor bitmapProcessor) {
        this.cacheManager = cacheManager;
        this.bitmapProcessor = bitmapProcessor;
    }

    @Override
    public Result retrieve(T tagWrapper) {
        Bitmap bitmap = innerRetrieve(tagWrapper);
        Bitmap elaborated = bitmapProcessor.elaborate(tagWrapper, bitmap);
        if (elaborated == null) {
            return new Failure();
        }
        return new Success(elaborated);
    }

    private Bitmap innerRetrieve(T tagWrapper) {
        return cacheManager.get(tagWrapper.getSourceUrl(), IGNORED, IGNORED);
    }

    public static class Success extends com.novoda.pxfetcher.task.Success {
        public Success(Bitmap bitmap) {
            super(bitmap);
        }
    }

    public static class Failure extends com.novoda.pxfetcher.task.Failure {

    }

}
