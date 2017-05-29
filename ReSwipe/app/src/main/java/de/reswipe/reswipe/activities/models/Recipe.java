package de.reswipe.reswipe.activities.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wedan on 29.05.2017.
 */

public class Recipe {

    public String name;
    public String image;
    public Bitmap downloadedImage;

    public Recipe (String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Recipe () { }

    public void loadImage() {
            try {
                URL url = new URL(this.image);
                try {
                    this.downloadedImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
    }
}
