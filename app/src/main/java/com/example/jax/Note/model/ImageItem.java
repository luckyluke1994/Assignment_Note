package com.example.jax.Note.model;

import android.graphics.Bitmap;

/**
 * Created by Jax on 08-Dec-16.
 */

public class ImageItem {
    private Bitmap image;

    public ImageItem(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}