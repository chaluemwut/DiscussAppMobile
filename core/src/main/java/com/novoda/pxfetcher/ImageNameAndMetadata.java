package com.novoda.pxfetcher;

public class ImageNameAndMetadata<T> {

    private final String fileName;
    private final T metadata;

    public ImageNameAndMetadata(String fileName, T metadata) {
        this.fileName = fileName;
        this.metadata = metadata;
    }

    public String getFileName() {
        return fileName;
    }

    public T getMetadata() {
        return metadata;
    }
}
