package com.novoda.pxfetcher;

import android.content.res.Resources;

import com.novoda.pxfetcher.task.Retriever;
import com.novoda.pxfetcher.task.TagWrapper;

public class RetrieverFactory<T extends TagWrapper<V>, V> {

    private static final double DEFAULT_ALLOWED_STRETCHING_THRESHOLD = .10;
    private final CacheManager cacheManager;
    private final FileNameFactory<V> fileManager;
    private final ResourceManager resourceManager;
    private final int maxDownsampling;
    private final Resources resources;

    public RetrieverFactory(CacheManager cacheManager, FileNameFactory<V> fileNameFactory, ResourceManager resourceManager, int maxDownsampling, Resources resources) {
        this.cacheManager = cacheManager;
        this.fileManager = fileNameFactory;
        this.resourceManager = resourceManager;
        this.maxDownsampling = maxDownsampling;
        this.resources = resources;
    }

    public Retriever<T, V> createDefaultRetriever() {
        Retriever memoryRetriever = createMemoryRetriever();
        Retriever fileRetriever = createFileRetriever();
        Retriever networkRetriever = createNetworkRetriever();
        return new FallbackStrategyRetriever<T, V>(memoryRetriever, fileRetriever, networkRetriever);
    }

    public Retriever<T, V> createMemoryRetriever() {
        BitmapProcessor bitmapProcessor = new TagWrapperValidityCheckBitmapProcessor();
        return new MemoryRetriever<T, V>(cacheManager, bitmapProcessor);
    }

    public Retriever<T, V> createNetworkRetriever() {
        Retriever fileRetriever = createFileRetriever();
        BitmapDecoder croppedDecoder = new DownsamplingBitmapDecoder(resources, DEFAULT_ALLOWED_STRETCHING_THRESHOLD, maxDownsampling);
        Retriever networkRetriever = new NetworkRetriever<T, V>(resourceManager, fileManager, croppedDecoder, new DummyBitmapProcessor());
        return new FallbackStrategyRetriever<T, V>(fileRetriever, networkRetriever);
    }

    public Retriever createFileRetriever() {
        BitmapDecoder croppedDecoder = new DownsamplingBitmapDecoder(resources, DEFAULT_ALLOWED_STRETCHING_THRESHOLD, maxDownsampling);
        return new FileRetriever<T, V>(fileManager, croppedDecoder, new DummyBitmapProcessor());
    }

}
