package com.dennistjahyadigotama.soaya.activities.ThreadEditActivity.adapter;

import android.net.Uri;

/**
 * Created by Denn on 7/4/2016.
 */
public class ImageGetter {
    String id;
    String caption;
    String encoded;
    String imageUrl;
    Uri uri;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
