package com.example.eugene.vkphotos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.eugene.vkphotos.imageloader.ImageLoaderImpl;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPhotoArray;

/**
 * Created by Eugene on 04.09.2016.
 */
public class ImagesGridActivity extends Activity {

    private ImagesAdapter imagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_images_grid);
        imagesAdapter = new ImagesAdapter(this, new ImageLoaderImpl());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.grid_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(imagesAdapter);
        loadPhotoUrls();
    }

    private void updateAdapter(VKPhotoArray vkApiPhotos) {
        imagesAdapter.setPhotos(vkApiPhotos);
        imagesAdapter.notifyDataSetChanged();
    }

    private void showError(String errorMessage) {
        Utils.showMessage(this, errorMessage);
    }

    private void loadAlbumPhotos(int albumId) {
        VKRequest getPhotos = new VKRequest("photos.get",
                VKParameters.from(VKApiConst.ALBUM_ID, albumId), VKPhotoArray.class);
        getPhotos.executeWithListener(new VKRequest.VKRequestListener() {
                                          @Override
                                          public void onComplete(VKResponse response) {
                                              updateAdapter((VKPhotoArray) response.parsedModel);
                                          }

                                          @Override
                                          public void onError(VKError error) {
                                              showError(error.errorMessage);
                                          }
                                      }
        );
    }

    private void loadPhotoUrls() {
        VKRequest getAlbums = new VKRequest("photos.getAlbums");
        getAlbums.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiPhotoAlbum> albums = new VKList<>();
                albums.fill(response.json, VKApiPhotoAlbum.class);
                // load album with max photos count
                int maxPhotosAlbumId = -1;
                int maxPhotosCount = 0;
                for (VKApiPhotoAlbum album : albums) {
                    if (album.size > maxPhotosCount) {
                        maxPhotosCount = album.size;
                        maxPhotosAlbumId = album.id;
                    }
                }
                if (maxPhotosAlbumId >= 0) {
                    loadAlbumPhotos(maxPhotosAlbumId);
                }
            }

            @Override
            public void onError(VKError error) {
                showError(error.errorMessage);
            }
        });
    }
}
