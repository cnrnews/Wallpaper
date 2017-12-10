package cnaiow.imooc.com.wallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

/**
*作者:candy
*创建时间:2017/12/10 10:18
*邮箱:1601796593@qq.com
*功能描述:壁纸service
**/
public class VideoLiveWallpaper extends WallpaperService {
    public static final String VIDEO_PARAMS_CONTROL_ACTION = "com.zhy.livewallpaper";
    public static final String KEY_ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 110;
    public static final int ACTION_VOICE_NORMAL = 111;

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }
    public static void setWallPaper(Context context) {
        final Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context, VideoLiveWallpaper.class));
        context.startActivity(intent);
    }
    class VideoEngine extends Engine {

        private MediaPlayer mMediaPlayer;

        private BroadcastReceiver mBroadcastReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            IntentFilter intentFilter = new IntentFilter(VIDEO_PARAMS_CONTROL_ACTION);
            registerReceiver(mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int action = intent.getIntExtra(KEY_ACTION, -1);
                    Log.i("TAG", "onReceive: "+action);
                    if (action == ACTION_VOICE_NORMAL) {
                        mMediaPlayer.setVolume(1, 1);
                    } else {
                        mMediaPlayer.setVolume(0, 0);
                    }
                }
            }, intentFilter);
        }

        @Override
        public void onDestroy() {
            unregisterReceiver(mBroadcastReceiver);
            super.onDestroy();

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(holder.getSurface());
            AssetManager assetManager = getApplication().getAssets();
            try {
                AssetFileDescriptor assetFileDescriptor = assetManager.openFd("test1.mp4");
                mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                        assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0, 0);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mMediaPlayer.reset();
            mMediaPlayer = null;
        }
    }

    /**
     * 静音
     * @param context
     */
    public static void voiceSilence(Context context)
    {
        Intent intent=new Intent(VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(KEY_ACTION,ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);

    }
    /**
     * 正常
     * @param context
     */
    public static void voiceNormal(Context context)
    {
        Intent intent=new Intent(VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(KEY_ACTION,ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);

    }
}
