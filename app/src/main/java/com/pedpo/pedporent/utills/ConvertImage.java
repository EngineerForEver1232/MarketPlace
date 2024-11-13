package com.pedpo.pedporent.utills;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ConvertImage {

    private ConvertImage() {
    }

    private static ConvertImage convertImage = new ConvertImage();

    public static ConvertImage newInstance() {
        return convertImage;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getImagePath(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }
}
