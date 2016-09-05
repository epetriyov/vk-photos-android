package com.example.eugene.vkphotos.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Eugene on 04.09.2016.
 */
public class ImageLoaderImpl implements ImageLoader {
    private static final int HTTP_OK = 200;
    private final LruCache<String, Bitmap> mMemoryCache;
    private final ExecutorService executorService;
    private final Handler handler;

    public ImageLoaderImpl() {
        executorService = Executors.newCachedThreadPool();
        handler = new Handler(Looper.getMainLooper());
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private static Bitmap getBitmap(byte[] response, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(response, 0, response.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(response, 0, response.length, options);
    }

    private static byte[] downloadImage(String imageUrl) throws IOException {
        byte[] response = null;
        URL url = new URL(imageUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        int statusCode = connection.getResponseCode();
        if (statusCode == HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.flush();
            response = outputStream.toByteArray();
            outputStream.close();
        }
        return response;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        synchronized (mMemoryCache) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public void loadImage(final String imageUrl, ImageView imageView, final int width, final int height) {
        if (imageUrl != null) {
            executorService.execute(new RunnableLoader(imageUrl, imageView, width, height));
        }
    }

    private class RunnableLoader implements Runnable {

        private final WeakReference<ImageView> imageViewWeakReference;

        private final int width;

        private final int height;

        private final String imageUrl;

        public RunnableLoader(String imageUrl, ImageView imageView, int width, int height) {
            this.imageViewWeakReference = new WeakReference<>(imageView);
            this.width = width;
            this.height = height;
            this.imageUrl = imageUrl;
        }

        @Override
        public void run() {
            Bitmap bitmap = getBitmapFromMemCache(imageUrl);
            if (bitmap == null) {
                byte[] imageBytes = null;
                try {
                    imageBytes = downloadImage(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (imageBytes != null) {
                    bitmap = getBitmap(imageBytes, width, height);
                    addBitmapToMemoryCache(imageUrl, bitmap);
                    postBitmap(bitmap);
                }
            } else {
                postBitmap(bitmap);
            }
        }

        private void postBitmap(final Bitmap bitmap) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (imageViewWeakReference.get() != null) {
                        imageViewWeakReference.get().setImageBitmap(bitmap);
                    }
                }
            });
        }
    }


}
