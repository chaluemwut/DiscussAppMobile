package com.novoda.pxfetcher;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CachedImagesFinder<T> {

    private final FileNameFactory<T> fileNameFactory;
    private final MetadataFactory<T> metadataFactory;

    public CachedImagesFinder(FileNameFactory<T> fileNameFactory, MetadataFactory<T> metadataFactory) {
        this.fileNameFactory = fileNameFactory;
        this.metadataFactory = metadataFactory;
    }

    public boolean hasCachedImage(String url, T metadata) {
        String fileName = fileNameFactory.getFileName(url, metadata);
        return new File(fileName).exists();
    }

    public List<ImageNameAndMetadata<T>> retrieveCachedImages(String cacheDirectory) throws FileNotFoundException {
        final File cacheDir = new File(cacheDirectory);
        if (!cacheDir.exists()) {
            throw new FileNotFoundException("Cache directory not found: " + cacheDirectory);
        }

        if (!cacheDir.isDirectory()) {
            throw new IllegalStateException("Path " + cacheDirectory + " is not a directory");
        }

        File[] files = cacheDir.listFiles(hasMetadata());
        List<ImageNameAndMetadata<T>> cachedImages = new ArrayList<ImageNameAndMetadata<T>>();
        for (File file : files) {
            cachedImages.add(new ImageNameAndMetadata<T>(file.getAbsolutePath(), metadataFactory.fromFileName(file.getName())));
        }
        return cachedImages;
    }

    private FileFilter hasMetadata() {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return metadataFactory.canCreateFromFileName(pathname.getName());
            }
        };
    }

}
