package com.example.eugene.vkphotos;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import com.vk.sdk.api.model.VKApiPhoto;

/**
 * Created by Eugene on 04.09.2016.
 */
public final class Utils {

    private Utils() {

    }

    public static void showMessage(Context context, String message) {
        if (context == null) {
            throw new IllegalStateException("Context can't be null");
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getAvailableLargePhoto(VKApiPhoto vkApiPhoto) {
        if (vkApiPhoto != null) {
            if (!TextUtils.isEmpty(vkApiPhoto.photo_1280)) {
                return vkApiPhoto.photo_1280;
            }
            if (!TextUtils.isEmpty(vkApiPhoto.photo_807)) {
                return vkApiPhoto.photo_807;
            }
            if (!TextUtils.isEmpty(vkApiPhoto.photo_604)) {
                return vkApiPhoto.photo_604;
            }
        }
        return null;
    }

    public static String getAvailableSmallPhoto(VKApiPhoto vkApiPhoto) {
        if (vkApiPhoto != null) {
            if (!TextUtils.isEmpty(vkApiPhoto.photo_130)) {
                return vkApiPhoto.photo_130;
            }
            if (!TextUtils.isEmpty(vkApiPhoto.photo_75)) {
                return vkApiPhoto.photo_75;
            }
        }
        return null;
    }

}
