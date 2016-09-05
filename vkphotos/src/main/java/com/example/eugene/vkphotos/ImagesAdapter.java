package com.example.eugene.vkphotos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.eugene.vkphotos.imageloader.ImageLoader;
import com.vk.sdk.api.model.VKPhotoArray;

/**
 * Created by Eugene on 04.09.2016.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private Context context;

    private VKPhotoArray photoArray;

    private ImageLoader imageLoader;

    private final int width;
    private final int height;

    public ImagesAdapter(Context context, ImageLoader imageLoader) {
        this.context = context;
        this.imageLoader = imageLoader;
        this.width = this.height = (int) context.getResources().getDimension(R.dimen.image_size);
    }

    public void setPhotos(VKPhotoArray photoArray) {
        this.photoArray = photoArray;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(width, height));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder imageViewHolder, int position) {
        imageViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return photoArray != null ? photoArray.getCount() : 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final int position) {
            ImageView imageView = (ImageView) itemView;
            imageView.setImageBitmap(null);
            imageLoader.loadImage(Utils.getAvailableSmallPhoto(photoArray.get(position)), imageView, width, height);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ImageDetailsActivity.buildIntent(v.getContext(), photoArray, position);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
