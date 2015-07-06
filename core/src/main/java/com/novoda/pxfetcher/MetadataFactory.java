package com.novoda.pxfetcher;

public interface MetadataFactory<T> {
    T fromFileName(String fileName);

    boolean canCreateFromFileName(String fileName);
}
