package com.novoda.pxfetcher;

import android.widget.ImageView;

import com.novoda.pxfetcher.task.Failure;
import com.novoda.pxfetcher.task.Success;

import java.lang.ref.WeakReference;

public class DayDreamImageViewCallback implements BitmapLoader.Callback {

    private final WeakReference<ImageView> imageViewWeakReference;
    private final int errorResourceId;
    private final ImageSetter imageSetter;

    public DayDreamImageViewCallback(ImageView imageView, int errorResourceId, ImageSetter imageSetter) {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        this.errorResourceId = errorResourceId;
        this.imageSetter = imageSetter;
    }

    @Override
    public void onStart() {
        // Don't set placeholder because it breaks the animation
    }

    @Override
    public void onResult(Success ok) {
        ImageView imageView = imageViewWeakReference.get();
        if (imageView == null) {
            return;
        }

        imageSetter.setBitmap(imageView, ok.getBitmap());
    }

    @Override
    public void onResult(Failure ko) {
        ImageView imageView = imageViewWeakReference.get();
        if (imageView == null) {
            return;
        }

        imageView.setImageResource(errorResourceId);
    }

}
