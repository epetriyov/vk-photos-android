package com.example.eugene.vkphotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.vk.sdk.api.model.VKPhotoArray;

/**
 * Created by Eugene on 04.09.2016.
 */
public class ImageDetailsActivity extends FragmentActivity {
    private static final String EXTRA_VK_PHOTOS = "vk_photos";
    private static final String EXTRA_POSITION = "position";
    private PagerAdapter adapter;

    public static Intent buildIntent(Context context, VKPhotoArray vkPhotoArray, int position) {
        Intent intent = new Intent(context, ImageDetailsActivity.class);
        intent.putExtra(EXTRA_VK_PHOTOS, vkPhotoArray);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKPhotoArray vkPhotoArray = getIntent().getParcelableExtra(EXTRA_VK_PHOTOS);
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        setContentView(R.layout.act_image_details);
        adapter = new ImagePagerAdapter(getSupportFragmentManager(), vkPhotoArray);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
    }

    public static class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final VKPhotoArray vkPhotoArray;

        public ImagePagerAdapter(FragmentManager fm, VKPhotoArray vkPhotoArray) {
            super(fm);
            this.vkPhotoArray = vkPhotoArray;
        }

        @Override
        public int getCount() {
            return vkPhotoArray != null ? vkPhotoArray.getCount() : 0;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(vkPhotoArray.get(position));
        }
    }
}
