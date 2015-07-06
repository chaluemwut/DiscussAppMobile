package com.novoda.pxfetcher;

import java.io.File;
import java.io.InputStream;

/**
 * A network manager is responsible of downloading images
 * given an http resource of url string.
 * At the moment requested resources can be retrieved as InputStream
 * or saved directly to a file.
 */
public interface ResourceManager {

    /**
     * Retrieves the image of the given url and stores the content as the file.
     * Throws an ImageNotFound exception when the url could not be resolved.
     *
     * @param url URL of the image to be downloaded.
     * @param f   File where the image should be stored.
     */
    void retrieveImage(String url, File f);

    /**
     * Returns the input stream for the given url
     * Throws an ImageNotFound exception when the url could not be resolved.
     *
     * @param url URL of the image to be downloaded
     * @return input stream of the image or null on error.
     */
    InputStream retrieveInputStream(String url);

}
