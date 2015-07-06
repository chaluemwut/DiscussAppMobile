package com.novoda.pxfetcher.task;

import com.novoda.pxfetcher.BitmapLoader;

public abstract class Result {
    public abstract void poke(BitmapLoader.Callback callback);
}
