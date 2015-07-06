package com.novoda.pxfetcher;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * A BitmapValidator validates whether a retrieved bitmap should be used for the given view.
 */
public interface BitmapValidator {
    boolean isBitmapSuitableForView(Bitmap bitmap, ImageView imageView);
}
