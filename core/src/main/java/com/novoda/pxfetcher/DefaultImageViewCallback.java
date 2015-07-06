package com.novoda.pxfetcher;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.novoda.pxfetcher.task.Failure;
import com.novoda.pxfetcher.task.Success;

import java.lang.ref.WeakReference;

public class DefaultImageViewCallback implements BitmapLoader.Callback {

    private final WeakReference<ImageView> imageViewWeakReference;
    private final int placeholderResourceId;
    private final Bitmap errorBitmap;
    private final ImageSetter imageSetter;

    public DefaultImageViewCallback(ImageView imageView, int placeholderResourceId, Bitmap errorBitmap, ImageSetter imageSetter) {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        this.placeholderResourceId = placeholderResourceId;
        this.errorBitmap = errorBitmap;
        this.imageSetter = imageSetter;
    }

    @Override
    public void onStart() {
        ImageView imageView = getImageView();
        Tag.toLoading(imageView);
        if (imageView == null) {
            return;
        }
        imageView.setImageResource(placeholderResourceId);
    }

    protected ImageView getImageView() {
        return imageViewWeakReference.get();
    }

    @Override
    public void onResult(Success ok) {
        ImageView imageView = getImageView();
        if (imageView == null) {
            return;
        }

        Bitmap bitmap = ok.getBitmap();
        imageSetter.setBitmap(imageView, bitmap);
        Tag.toSuccess(imageView);
    }

    @Override
    public void onResult(Failure ko) {
        ImageView imageView = getImageView();
        if (imageView == null) {
            return;
        }
        imageView.setImageBitmap(errorBitmap);
        Tag.toFailure(imageView);
    }

}
