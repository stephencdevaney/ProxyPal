package com.example.ppstart;


//This Utility class is used for converting the format of images used in the database.
//Images must be stored in the database as a BLOB data type. In order to do this, images that are bitmaps
//must first be converted to byte arrays before they can be inserted into the database, and vice versa;
//images queried from the database must first be converted from a byte array to a bitmap so they can be
//operated on using the Glide library.

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}