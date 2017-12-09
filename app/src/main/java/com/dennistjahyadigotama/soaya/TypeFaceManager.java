package com.dennistjahyadigotama.soaya;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Denn on 9/17/2016.
 */
public class TypeFaceManager {

    public static Typeface typeface;


    public static Typeface getFontAwesomeTypeface(Context context)
    {
        return typeface = Typeface.createFromAsset(context.getAssets(),"fontawesome-webfont.ttf");
    }
}
