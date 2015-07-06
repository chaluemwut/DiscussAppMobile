package com.novoda.pxfetcher;

import android.widget.ImageView;

public interface PixelFetcher<T> {

    void load(String url, ImageView view);

    void load(String url, T metadata, ImageView view);

}
