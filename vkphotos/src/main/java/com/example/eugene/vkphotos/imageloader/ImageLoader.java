package com.example.eugene.vkphotos.imageloader;

import android.widget.ImageView;

/**
 * Created by Eugene on 04.09.2016.
 */
public interface ImageLoader {

    void loadImage(String imageUrl, ImageView imageView, int width, int height);
}
