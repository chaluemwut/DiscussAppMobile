package com.novoda.pxfetcher.task;

import com.novoda.pxfetcher.BitmapLoader;

public abstract class Failure extends Result {
    @Override
    public void poke(BitmapLoader.Callback callback) {
        callback.onResult(this);
    }
}
