package com.novoda.pxfetcher;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpResourceManager implements ResourceManager {

    private static final String TAG = HttpResourceManager.class.getSimpleName();

    private static final int TEMP_REDIRECT = 307;
    private static final int CONNECTION_TIMEOUT_MILLIS = 10000;
    private static final int READ_TIMEOUT_MILLIS = 10000;
    private static final int BUFFER_SIZE = 60 * 1024;

    private int manualRedirects;

    @Override
    public void retrieveImage(String url, File f) {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection conn = null;
        try {
            conn = openConnection(url);
            conn.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
            conn.setReadTimeout(READ_TIMEOUT_MILLIS);

            if (conn.getResponseCode() == TEMP_REDIRECT) {
                redirectManually(f, conn);
            } else {
                is = conn.getInputStream();
                os = new FileOutputStream(f);
                copyStream(is, os);
            }
        } catch (FileNotFoundException fnfe) {
            throw new ImageNotFoundException();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            closeSilently(is);
            closeSilently(os);
        }
    }

    private void copyStream(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                int amountRead = is.read(buffer);
                if (amountRead == -1) {
                    break;
                }
                os.write(buffer, 0, amountRead);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG && Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Exception : " + e.getMessage());
            }
        }
    }

    private void closeSilently(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            Log.e(PixelFetcher.class.getSimpleName(), "Failed to close stream");
        }
    }

    private void redirectManually(File f, HttpURLConnection conn) {
        if (manualRedirects < 3) {
            manualRedirects++;
            retrieveImage(conn.getHeaderField("Location"), f);
        } else {
            manualRedirects = 0;
        }
    }

    @Override
    public InputStream retrieveInputStream(String url) {
        HttpURLConnection conn = null;
        try {
            conn = openConnection(url);
            conn.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
            conn.setReadTimeout(READ_TIMEOUT_MILLIS);
            return conn.getInputStream();
        } catch (FileNotFoundException fnfe) {
            throw new ImageNotFoundException();
        } catch (Throwable ex) {
            return null;
        }
    }

    private HttpURLConnection openConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

}
