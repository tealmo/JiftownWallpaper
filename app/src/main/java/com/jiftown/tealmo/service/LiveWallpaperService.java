package com.jiftown.tealmo.service;

import android.app.WallpaperManager;
import android.content.*;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;


/**
 * @Developer: tealmo
 * @Date: 2017-05-15 11:06 星期一
 */
public class LiveWallpaperService extends WallpaperService {
    private static final String TAG = "LiveWallpaperService";
    private static final String SERVICE_NAME = "com.jiftown.tealmo.service.LiveWallpaperService";
    public static final String KEY_ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 110;
    public static final int ACTION_VOICE_NORMAL = 111;
    private VideoWallpaperEngine wallpaperEngine;

    @Override
    public Engine onCreateEngine() {
        wallpaperEngine = new VideoWallpaperEngine();
        return wallpaperEngine;
    }

    public static void setVoiceSilence(Context context) {
        Intent intent = new Intent(SERVICE_NAME);
        intent.putExtra(KEY_ACTION, ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    public static void setVoiceNormal(Context context) {
        Intent intent = new Intent(SERVICE_NAME);
        intent.putExtra(KEY_ACTION, ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    public static void setVideoWallpaper(Context context) {
        final Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context, LiveWallpaperService.class));
        context.startActivity(intent);
    }

    private class VideoWallpaperEngine extends Engine {
        private MediaPlayer mediaPlayer;
        private BroadcastReceiver broadcastReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            Log.d(TAG, "Engine onCreate");
            IntentFilter intentFilter = new IntentFilter(SERVICE_NAME);
            registerReceiver(broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(TAG, "onReceive");
                    int action = intent.getIntExtra(KEY_ACTION, -1);
                    switch (action) {
                        case ACTION_VOICE_NORMAL:
                            mediaPlayer.setVolume(1.0f, 1.0f);
                        case ACTION_VOICE_SILENCE:
                            mediaPlayer.setVolume(0.0f, 0.0f);
                    }
                }
            }, intentFilter);
        }

        @Override
        public void onDestroy() {
            Log.d(TAG, "onDestroy");
            super.onDestroy();
            unregisterReceiver(broadcastReceiver);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            Log.d(TAG, "onVisibilityChanged: " + visible);
            if (visible)
                mediaPlayer.start();
            else
                mediaPlayer.pause();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setSurface(holder.getSurface());
            try {
                AssetFileDescriptor fd = getAssets().openFd("start.mp4");
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0, 0);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }


        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
