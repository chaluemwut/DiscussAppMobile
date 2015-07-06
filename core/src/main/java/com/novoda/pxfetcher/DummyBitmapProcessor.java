package com.novoda.pxfetcher;

import android.graphics.Bitmap;

import com.novoda.pxfetcher.task.TagWrapper;

/**
 * This bitmap processor simply forwards the received bitmap as output of its elaboration.
 * This class can be used when no real operation is necessary as last stage of a retrieve operation.
 * 
 */
public class DummyBitmapProcessor implements BitmapProcessor {

    @Override
    public Bitmap elaborate(TagWrapper tagWrapper, Bitmap bitmap) {
        return bitmap;
    }

}
