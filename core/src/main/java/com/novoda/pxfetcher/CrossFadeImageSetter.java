package com.novoda.pxfetcher;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class CrossFadeImageSetter implements ImageSetter {

    private final int animationDuration;

    public CrossFadeImageSetter(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    @Override
    public void setBitmap(ImageView imageView, Bitmap bitmap) {
        animateTo(imageView, new BitmapDrawable(imageView.getResources(), bitmap));
    }

    protected void animateTo(ImageView imageView, Drawable nextDrawable) {
        Drawable currentDrawable = imageView.getDrawable();
        if (currentDrawable == null) {
            currentDrawable = new ColorDrawable(0x00000000);
        }
        if (currentDrawable instanceof CrossFadeDrawable) {
            Drawable destinationCurrentDrawable = ((CrossFadeDrawable) currentDrawable).getDestinationDrawable();
            setTransitionAndStart(imageView, destinationCurrentDrawable, nextDrawable);
        } else {
            setTransitionAndStart(imageView, currentDrawable, nextDrawable);
        }
    }

    protected void setTransitionAndStart(ImageView imageView, Drawable prevDrawable, Drawable nextDrawable) {
        CrossFadeDrawable crossFadeDrawable = CrossFadeDrawable.create(prevDrawable, nextDrawable);
        imageView.setImageDrawable(crossFadeDrawable);
        crossFadeDrawable.setCrossFadeEnabled(true);
        crossFadeDrawable.startTransition(animationDuration);
    }

}
