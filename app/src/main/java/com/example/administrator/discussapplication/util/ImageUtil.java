package com.example.administrator.discussapplication.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {

    public static Bitmap getResizedBitmap(Bitmap bm, int reduceSize){
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) width) / reduceSize;
        float scaleHeight = ((float) height) / reduceSize;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

}
