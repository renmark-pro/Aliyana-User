package com.aliyanaresorts.aliyanahotelresorts.service.database;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements
        ImageLoader.ImageCache {

//    public static int getDefaultLruCacheSize() {
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//
//        return maxMemory / 8;
//    }

//    public LruBitmapCache() {
//        this(getDefaultLruCacheSize());
//    }

    private LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
