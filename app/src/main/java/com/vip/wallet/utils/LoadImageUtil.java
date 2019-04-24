package com.vip.wallet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * 创建者     金国栋
 * 创建时间   2018/1/12 10:14
 * 描述	      ${TODO}
 */
public class LoadImageUtil {

    public static void loadNetImage(@NonNull Context context, String url, @NonNull ImageView targetView) {
        loadNetImage(context, url, targetView, null);
    }

    public static void loadNetImage(@NonNull Context context, String url, @NonNull ImageView targetView,
                                    @Nullable RequestListener<Drawable> requestListener) {
        loadNetImage(context, url, targetView, 0, 0, requestListener);
    }

    public static void loadNetImage(@NonNull Context context, String url, @NonNull ImageView targetView,
                                    @DrawableRes int resPlaceHolder, @DrawableRes int resError) {
        loadNetImage(context, url, targetView, resPlaceHolder, resError, null);
    }

    public static void loadNetImage(@NonNull Context context, String url, @NonNull ImageView targetView,
                                    @DrawableRes int resPlaceHolder, @DrawableRes int resError,
                                    @Nullable RequestListener<Drawable> requestListener) {
        loadImage(context, url, targetView, resPlaceHolder, resError, requestListener);
    }

    public static void loadNetImage(@NonNull Context context, String url, @NonNull ImageView targetView,
                                    @DrawableRes int resPlaceHolder, @DrawableRes int resError,
                                    @Nullable RequestListener<Drawable> requestListener, int timeout) {
        loadImage(context, url, targetView, resPlaceHolder, resError, requestListener, timeout);
    }

    public static void loadImage(@NonNull Context context, Object object, @NonNull ImageView targetView,
                                 @DrawableRes int resPlaceHolder, @DrawableRes int resError,
                                 @Nullable RequestListener<Drawable> requestListener) {
        loadImage(context, object, targetView, resPlaceHolder, resError, requestListener, 0);
    }

    public static void loadImage(@NonNull Context context, Object object, @NonNull ImageView targetView,
                                 @DrawableRes int resPlaceHolder, @DrawableRes int resError,
                                 @Nullable RequestListener<Drawable> requestListener, int timeout) {
        Glide.with(context).setDefaultRequestOptions(new RequestOptions()
                .placeholder(resPlaceHolder)
                .timeout(timeout)
                .fallback(resError)
                .error(resError)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(object)
                .listener(requestListener)
                .into(targetView);
    }

    public static void loadBitmapImage(@NonNull Context context, Bitmap bitmap, @NonNull ImageView targetView) {
        loadImage(context, bitmap, targetView, 0, 0, null);
    }

    public static void loadResImage(@NonNull Context context, @DrawableRes int resId,
                                    @NonNull ImageView targetView) {
        loadImage(context, resId, targetView, 0, 0, null);
    }

    public static void loadFileImage(@NonNull Context context, File file, ImageView targetView) {
        loadFileImage(context, file, targetView, null);
    }

    public static void loadFileImage(@NonNull Context context, File file, ImageView targetView,
                                     @Nullable RequestListener<Drawable> requestListener) {
        loadImage(context, file, targetView, 0, 0, requestListener);
    }

    public static void clearDiskCache(@NonNull Context context) {
        Glide.get(context).clearDiskCache();
    }

    public static void clearMemoryCache(@NonNull Context context) {
        Glide.get(context).clearMemory();
    }

    public static void clearAllCache(@NonNull Context context) {
        Glide.get(context).clearDiskCache();
        Glide.get(context).clearMemory();
    }

    public static long getCacheSize(Context context) {
        return new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR).getUsableSpace();
    }

    public interface ClearCacheListener {
        void clearFinish();
    }
}
