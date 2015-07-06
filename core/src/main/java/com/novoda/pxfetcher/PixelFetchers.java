package com.novoda.pxfetcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.novoda.pxfetcher.task.Result;
import com.novoda.pxfetcher.task.Retriever;
import com.novoda.pxfetcher.task.Success;
import com.novoda.pxfetcher.task.TagWrapper;

import java.io.File;

public class PixelFetchers {

    private static PixelFetcher instance;

    private PixelFetchers() {
        throw new IllegalStateException("This class should have no instances.");
    }

    public static PixelFetcher getInstance(Context context) {
        if (instance == null) {
            instance = DefaultPixelFetcher.newInstance(context);
        }
        return instance;
    }

    private static class DefaultPixelFetcher implements PixelFetcher<Void> {

        private static final int PERCENTAGE_OF_MEMORY_FOR_CACHE = 15;
        private static final int PLACEHOLDER_RES_ID = 0;
        private static final int MAX_DOWNSAMPLING_FACTOR = 2;

        private final ImageViewCallbackFactory callbackFactory;
        private final BitmapLoader bitmapLoader;
        private final Retriever<TagWrapper<Void>, Void> memoryRetriever;

        private DefaultPixelFetcher(ImageViewCallbackFactory callbackFactory, BitmapLoader bitmapLoader, Retriever<TagWrapper<Void>, Void> memoryRetriever) {
            this.callbackFactory = callbackFactory;
            this.bitmapLoader = bitmapLoader;
            this.memoryRetriever = memoryRetriever;
        }

        public static DefaultPixelFetcher newInstance(Context context) {
            ImageViewCallbackFactory callbackFactory = new ImageViewCallbackFactory(new DefaultImageSetter());

            CacheManager memoryCacheManager = new BetterMemoryCache(PERCENTAGE_OF_MEMORY_FOR_CACHE);
            ResourceManager resourceManager = new HttpResourceManager();
            FileNameFactory fileNameFactory = new DefaultFileNameFactory(context.getCacheDir().getAbsolutePath());

            RetrieverFactory<TagWrapper<Void>, Void> retrieverFactory = new RetrieverFactory<TagWrapper<Void>, Void>(
                    memoryCacheManager,
                    fileNameFactory,
                    resourceManager,
                    MAX_DOWNSAMPLING_FACTOR,
                    context.getResources()
            );

            Retriever<TagWrapper<Void>, Void> defaultRetriever = retrieverFactory.createDefaultRetriever();
            Retriever<TagWrapper<Void>, Void> memoryRetriever = retrieverFactory.createMemoryRetriever();
            BitmapLoader bitmapLoader = new BitmapLoader(defaultRetriever);

            return new DefaultPixelFetcher(callbackFactory, bitmapLoader, memoryRetriever);
        }

        @Override
        public void load(String url, ImageView view) {
            load(url, null, view);
        }

        @Override
        public void load(String url, Void metadata, ImageView view) {
            if (Tag.shouldSkip(url, view)) {
                return;
            }

            TagWrapper<Void> tagWrapper = new DefaultTagWrapper(url);
            view.setTag(tagWrapper.getTag());

            BitmapLoader.Callback callback = callbackFactory.createCallback(view);
            Result retrieved = memoryRetriever.retrieve(tagWrapper);
            if (retrieved instanceof Success) {
                retrieved.poke(callback);
            } else {
                bitmapLoader.load(tagWrapper, callback);
            }
        }

    }

    private static class DefaultFileNameFactory implements FileNameFactory<Void> {

        private static final String TAG = BuildConfig.APPLICATION_ID;
        private static final String DELIMITER = "_";
        private static final String FILE_NAME_FORMAT = TAG + DELIMITER + "%s";

        private final String cacheDirectory;

        public DefaultFileNameFactory(String cacheDirectory) {
            this.cacheDirectory = cacheDirectory;
        }

        @Override
        public String getFileName(String sourceUrl, Void metadata) {
            String fileName = String.format(FILE_NAME_FORMAT, sourceUrl.hashCode());
            return cacheDirectory + File.separator + fileName;
        }
    }

    private static class DefaultImageSetter implements ImageSetter {

        @Override
        public void setBitmap(ImageView imageView, Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

    }

}
