package com.novoda.pxfetcher;

/**
 * A FileNameFactory is responsible to create a file name for a retrieved resource.
 * @param <T> type of meta data
 */
public interface FileNameFactory<T> {

    String getFileName(String sourceUrl, T metadata);

}
