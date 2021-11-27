package edu.byu.cs.client.view.cache;

import android.graphics.drawable.Drawable;
import domain.User;

import java.util.HashMap;
import java.util.Map;

public class ImageCache {

    private static ImageCache instance;

    private final Map<User, Drawable> images = new HashMap<>();

    public static ImageCache getInstance() {
        if(instance == null) {
            instance = new ImageCache();
        }

        return instance;
    }

    private ImageCache() {}

    public Drawable getImageDrawable(User user) {
        return images.get(user);
    }

    public void cacheImage(User user, Drawable drawable) {
        images.put(user, drawable);
    }
}
