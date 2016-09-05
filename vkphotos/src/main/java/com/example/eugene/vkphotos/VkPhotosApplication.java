package com.example.eugene.vkphotos;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by Eugene on 04.09.2016.
 */
public class VkPhotosApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
