package com.example.eugene.vkphotos;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.eugene.vkphotos.imageloader.ImageLoader;
import com.example.eugene.vkphotos.imageloader.ImageLoaderProvider;
import com.vk.sdk.api.model.VKApiPhoto;

/**
 * Created by Eugene on 04.09.2016.
 */
public class ImageDetailFragment extends Fragment {
    private static final String EXTRA_VK_API_PHOTO = "vk_api_photo";

    public static Fragment newInstance(VKApiPhoto vkApiPhoto) {
        Fragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_VK_API_PHOTO, vkApiPhoto);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageLoader getImageLoader() {
        if (getActivity() != null && getActivity() instanceof ImageLoaderProvider) {
            return ((ImageLoaderProvider) getActivity()).getImageLoader();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_image_details, container, false);
        final VKApiPhoto vkApiPhoto = getArguments().getParcelable(EXTRA_VK_API_PHOTO);
        final ImageLoader imageLoader = getImageLoader();
        if (vkApiPhoto != null && imageLoader != null) {
            final ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    imageLoader.loadImage(Utils.getAvailableLargePhoto(vkApiPhoto), imageView, imageView.getWidth(), imageView.getHeight());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
        return view;
    }
}
