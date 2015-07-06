package com.novoda.pxfetcher;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.widget.ImageView;

import com.novoda.pxfetcher.task.Failure;
import com.novoda.pxfetcher.task.Success;

import java.lang.ref.WeakReference;

public class DebugImageViewCallback implements BitmapLoader.Callback {

    private final WeakReference<ImageView> imageViewWeakReference;
    private final int placeholderResourceId;
    private final int errorResourceId;
    private final ImageSetter imageSetter;

    public DebugImageViewCallback(ImageView imageView, int placeholderResourceId, int errorResourceId, ImageSetter imageSetter) {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        this.placeholderResourceId = placeholderResourceId;
        this.errorResourceId = errorResourceId;
        this.imageSetter = imageSetter;
    }

    @Override
    public void onStart() {
        ImageView imageView = imageViewWeakReference.get();
        if (imageView == null) {
            return;
        }

        imageView.setColorFilter(null);
        imageView.setImageResource(placeholderResourceId);
    }

    @Override
    public void onResult(Success ok) {
        ImageView imageView = imageViewWeakReference.get();
        if (imageView == null) {
            return;
        }

        Resources resources = imageView.getResources();
        int color;
        if (ok instanceof MemoryRetriever.Success) {
            color = resources.getColor(R.color.px__debug_imageloader_memory_overlay);
        } else if (ok instanceof FileRetriever.Success) {
            color = resources.getColor(R.color.px__debug_imageloader_file_overlay);
        } else {
            color = resources.getColor(R.color.px__debug_imageloader_network_overlay);
        }

        imageView.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
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
