package cnaiow.imooc.com.wallpaper;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCheckBox = findViewById(R.id.cb_taggle);


        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    VideoLiveWallpaper.voiceSilence(getApplicationContext());
                    mCheckBox.setText("正常");
                } else {
                    VideoLiveWallpaper.voiceNormal(getApplicationContext());
                    mCheckBox.setText("静音");
                }
            }
        });
    }

    public void setWallPaper(View view) {
        Toast.makeText(this,"fa",Toast.LENGTH_SHORT).show();
        VideoLiveWallpaper.setWallPaper(this);

    }
}
