package com.jiftown.tealmo.ui;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.jiftown.tealmo.R;
import com.jiftown.tealmo.utils.Utils;

import java.io.IOException;

public class VideoPlayerActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private Button btn;
    private MediaPlayer player;
    private SurfaceViewCallback surfaceViewCallback;
    private ImageView thumbnail;
//    private ImageButton playPause;
    private String url = "http://192.168.1.103:8080/VideoCenter/video/start.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        findViewById();
        initView();
    }
    private void findViewById() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        btn = (Button) findViewById(R.id.play_pause);
        thumbnail= (ImageView) findViewById(R.id.thumbnail);
    }
    private void initView() {
        player = new MediaPlayer();
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(surfaceViewCallback=new SurfaceViewCallback());
        Bitmap bitmap=Utils.getVideoThumbnail(url);
        thumbnail.setImageBitmap(bitmap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    surfaceViewCallback.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class SurfaceViewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            player.release();
        }
        public void play() throws IOException {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            AssetFileDescriptor fd=getAssets().openFd("start.mp4");
//            player.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());
            player.setDataSource(url);
            player.setLooping(true);
            player.setDisplay(surfaceView.getHolder());
            player.prepareAsync();  //通过一部的方式加载媒体资源
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
        }
    }

}

