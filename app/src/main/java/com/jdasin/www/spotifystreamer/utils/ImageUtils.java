package com.jdasin.www.spotifystreamer.utils;

import android.content.Context;

/**
 * Created by Daniel on 7/5/2015.
 */
public class ImageUtils {
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
