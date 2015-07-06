package com.novoda.pxfetcher;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class RatioBitmapValidator implements BitmapValidator {

    private final double allowedRatioChangeTreshold;

    public RatioBitmapValidator(double allowedRatioChangeTreshold) {
        this.allowedRatioChangeTreshold = allowedRatioChangeTreshold;
    }

    @Override
    public boolean isBitmapSuitableForView(Bitmap bitmap, ImageView imageView) {
        if (imageView == null || bitmap == null) {
            return false;
        }

        if (isRatioIntegrityIntact(imageView, bitmap)) {
            return true;
        }
        return false;
    }

    private boolean isRatioIntegrityIntact(View view, Bitmap bitmap) {
        double viewRatio = 1.0 * view.getMeasuredWidth() / view.getMeasuredHeight();
        double bmpRatio = 1.0 * bitmap.getWidth() / bitmap.getHeight();
        double delta = Math.abs(viewRatio - bmpRatio);
        return delta < allowedRatioChangeTreshold;
    }

}
