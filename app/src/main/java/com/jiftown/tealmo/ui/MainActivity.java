package com.jiftown.tealmo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.jiftown.tealmo.R;
import com.jiftown.tealmo.service.LiveWallpaperService;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_SET_WALLPAPER = 0x001;
    private Button joinBtn;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        setWidgetListener();

    }

    private void findViewById() {
        joinBtn = (Button) findViewById(R.id.btn_join);
        checkBox = (CheckBox) findViewById(R.id.has_voice);
    }

    private void setWidgetListener() {
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveWallpaperService.setVideoWallpaper(getApplicationContext());
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "静音", Toast.LENGTH_SHORT).show();
                    LiveWallpaperService.setVoiceSilence(getApplicationContext());
                } else {
                    Toast.makeText(MainActivity.this, "正常", Toast.LENGTH_SHORT).show();
                    LiveWallpaperService.setVoiceNormal(getApplicationContext());
                }
            }
        });
    }


}
