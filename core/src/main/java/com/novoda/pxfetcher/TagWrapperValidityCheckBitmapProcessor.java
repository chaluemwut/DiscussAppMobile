package com.novoda.pxfetcher;

import android.graphics.Bitmap;
import com.novoda.pxfetcher.task.TagWrapper;

public class TagWrapperValidityCheckBitmapProcessor implements BitmapProcessor {

    /**
     * Just checks if the TagWrapper is still valid
     * ie: it still contains the reference to the ImageView to populate
     *
     * @param tagWrapper
     * @param bitmap
     * @return null if no image as input or the TagWrapper is not holding an ImageView reference no more
     */
    @Override
    public Bitmap elaborate(TagWrapper tagWrapper, Bitmap bitmap) {
        if (tagWrapper.isNoLongerValid() || bitmap == null) {
            return null;
        }
        return bitmap;
    }

}
