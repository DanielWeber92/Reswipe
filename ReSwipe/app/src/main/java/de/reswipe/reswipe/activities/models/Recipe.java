package de.reswipe.reswipe.activities.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

}
