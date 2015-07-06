package com.novoda.pxfetcher.task;

import android.graphics.Bitmap;

import com.novoda.pxfetcher.BitmapLoader;

public abstract class Success extends Result {

    private final Bitmap bitmap;

    public Success(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void poke(BitmapLoader.Callback callback) {
        callback.onResult(this);
    }

}
