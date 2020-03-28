package com.rohim.rohimmodule;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    private static String TAG = "ImageUtils";

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // General Image
    public void LoadRealImage(String uri, final ImageView image){

        Picasso.get().load(Uri.parse(uri)).into(image);
    }

    public void LoadRealImage(String url, final ImageView image, int width, int height){

        Picasso.get()
                .load(url)
                .resize(width, height)
                .into(image);
    }

    public void LoadRealImage(File file, final ImageView image, int width, int height){

        Picasso.get()
                .load(file)
                .resize(width, height)
                .into(image);
    }

    public void LoadRealImage(Context context, String uri, final ImageView image, int thumb){

        Picasso.get().load(Uri.parse(uri)).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(thumb)).placeholder(context.getResources().getDrawable(thumb)).into(image);
    }

    public void LoadRealImageNoCache(String uri, final ImageView image){

        Picasso.get().load(Uri.parse(uri)).into(image);
    }

    public void LoadRealImage(int uri, final ImageView image){

        Picasso.get().load(uri).into(image);
    }

    public void LoadProfileImage(String uri, final ImageView image){

        Picasso.get().load(Uri.parse(uri)).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).transform(new CircleTransform()).into(image);
    }

    public void LoadCircleRealImage(String uri, final ImageView image){

        Picasso.get().load(Uri.parse(uri)).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
    }

    public void LoadSquareImageHeaderSlider(Context context, String uri, final ImageView image, int size){

        Picasso.get().load(Uri.parse(uri)).networkPolicy(NetworkPolicy.NO_CACHE).resize(size, size).memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.drawable.user)).placeholder(context.getResources().getDrawable(R.drawable.user)).into(image);
    }

    public void LoadCustomSizedImage(Context context, int uri, final ImageView image, int width, int height){

        Picasso.get().load(uri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).resize(width, height).centerCrop().error(context.getResources().getDrawable(R.drawable.user)).placeholder(context.getResources().getDrawable(R.drawable.user)).into(image);
    }

    public void LoadCustomSizedImage(String uri, final ImageView image, int width, int height){

        Picasso.get().load(uri).networkPolicy(NetworkPolicy.NO_CACHE).resize(width, height).centerCrop().into(image);
    }

    public void LoadCategoryImage(String uri, final ImageView image){

        Picasso.get().load(Uri.parse(uri)).resize(120,120).into(image);
    }

    public void LoadCategoryImage(int uri, final ImageView image){

        Picasso.get().load(uri).centerInside().resize(120,120).into(image);
    }

    public static Bitmap decodeBitmap(Uri bitmapUri, ContentResolver resolver, int width, int height) throws IOException {
        InputStream is = resolver.openInputStream(bitmapUri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,options);
        is.close();

        int ratio = Math.min(options.outWidth/width, options.outHeight/height);
        int sampleSize = Integer.highestOneBit((int) Math.floor(ratio));
        if(sampleSize == 0){
            sampleSize = 1;
        }
        Log.d(TAG, "Image Size: " + sampleSize);

        options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        is = resolver.openInputStream(bitmapUri);
        Bitmap b = BitmapFactory.decodeStream(is,null,options);
        is.close();
        return b;
    }

}