package com.novoda.pxfetcher;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

public class CrossFadeDrawable extends TransitionDrawable {

    private static final int PREV_ID = 100;
    private static final int NEXT_ID = 101;

    public static CrossFadeDrawable create(Drawable prevDrawable, Drawable nextDrawable) {
        Drawable[] drawables = {
                prevDrawable,
                nextDrawable
        };
        CrossFadeDrawable drawable = new CrossFadeDrawable(drawables);
        drawable.setId(0, PREV_ID);
        drawable.setId(1, NEXT_ID);
        return drawable;
    }

    CrossFadeDrawable(Drawable[] layers) {
        super(layers);
    }

    public Drawable getDestinationDrawable() {
        return findDrawableByLayerId(NEXT_ID);
    }

}
