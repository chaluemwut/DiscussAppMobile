package com.novoda.pxfetcher;

import android.graphics.Bitmap;

import com.novoda.pxfetcher.task.TagWrapper;

public interface BitmapProcessor {

    Bitmap elaborate(TagWrapper tagWrapper, Bitmap bitmap);

}
