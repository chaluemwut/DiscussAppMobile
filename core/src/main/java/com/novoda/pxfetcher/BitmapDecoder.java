package com.novoda.pxfetcher;

import android.graphics.Bitmap;

import com.novoda.pxfetcher.task.TagWrapper;

import java.io.File;

public interface BitmapDecoder {

    Bitmap decode(TagWrapper tagWrapper, File file);

}
