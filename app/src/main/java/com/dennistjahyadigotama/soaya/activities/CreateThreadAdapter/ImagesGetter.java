package com.dennistjahyadigotama.soaya.activities.CreateThreadAdapter;

import android.net.Uri;

/**
 * Created by Denn on 6/30/2016.
 */
public class ImagesGetter {
    String caption;
    String encoded;
    Uri uri;

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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
