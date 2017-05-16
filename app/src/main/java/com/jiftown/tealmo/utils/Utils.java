package com.jiftown.tealmo.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

/**
 * @Developer: tealmo
 * @Date: 2017-05-16 17:30 星期二
 */
public class Utils {
    public static Bitmap getVideoThumbnail(String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(url);
            bitmap = retriever.getFrameAtTime(-1);
        } finally {
            retriever.release();
        }
        return bitmap;
    }
}
