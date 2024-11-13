package com.pedpo.pedporent.utills;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;


public class CompressImage {

    private static CompressImage compressImage = new CompressImage();

    public static CompressImage newInstance()
    {
        return compressImage;
    }

    public Bitmap ratio(Context context , Uri uri, float pixelW, float pixelH) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
//        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        Bitmap bitmap =null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;

        float hh = pixelH;
        float ww = pixelW;

        int be = 1;//be=1
        if (w > h && w > ww) {
            be = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (options.outHeight / hh);
        }
        if (be <= 0) be = 1;
        options.inSampleSize = be;

        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


//        return compress(bitmap, maxSize);
        return bitmap;
    }

}
