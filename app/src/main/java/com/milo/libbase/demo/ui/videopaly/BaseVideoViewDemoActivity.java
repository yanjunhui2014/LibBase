package com.milo.libbase.demo.ui.videopaly;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.milo.libbase.demo.R;
import com.milo.libbase.demo.data.Constants;
import com.milo.libbase.framework.OriginJump;
import com.milo.libbase.utils.Utils;
import com.milo.libbase.widget.videoview.BaseVideoViewBuilder;
import com.milo.libbase.widget.videoview.BottomBarListener;
import com.milo.libbase.widget.videoview.SimpleExoVideoView;
import com.milo.libbase.widget.videoview.TopBarListener;
import com.milo.libbase.widget.videoview.VideoCommonListener;
import com.milo.libbase.widget.videoview.VideoData;
import com.milo.libbase.widget.videoview.VideoDefinition;
import com.milo.libbase.widget.videoview.VideoStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/26
 */
public class BaseVideoViewDemoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DemoActivity";

    private SimpleExoVideoView mSimpleVideoView;

    public static OriginJump createJump(Context context) {
        return new OriginJump(context, BaseVideoViewDemoActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_video_view_demo);
        Log.d(TAG, "onCreate");

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        Map<VideoDefinition, VideoData> map = new HashMap<>();
        map.put(VideoDefinition.STANDARD, new VideoData(Constants.VIDEO_URL_1_STANDARD, "100kb"));
        map.put(VideoDefinition.HEIGHT, new VideoData(Constants.VIDEO_URL_2_STANDARD, "200kb"));
        map.put(VideoDefinition.SUPER, new VideoData(Constants.VIDEO_URL_1_SUPER, "300kb"));

        mSimpleVideoView = findViewById(R.id.mSimpleVideoView);
        findViewById(R.id.mBtnShowTopBar).setOnClickListener(this);
        findViewById(R.id.mBtnHideTopBar).setOnClickListener(this);
        findViewById(R.id.mBtnShowBottomBar).setOnClickListener(this);
        findViewById(R.id.mBtnHideBottomBar).setOnClickListener(this);
        mSimpleVideoView = new BaseVideoViewBuilder<SimpleExoVideoView>()
                .setVideoTitle("Test Video")
                .setTopBarListener(new TopBarListener() {
                    @Override
                    public void onBackClick(View v) {
                        onBackPressed();
                    }

                    @Override
                    public void onMenuClick(View v) {
                        Toast.makeText(BaseVideoViewDemoActivity.this, "menu click", Toast.LENGTH_SHORT).show();
                    }
                })
                .setBottomBarListener(new BottomBarListener() {
                    @Override
                    public void onWholeScreenClick(View v, int scaleType) {
                        if (scaleType == BottomBarListener.SCALE_TYPE_BIG) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        } else {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        }
                    }

                    @Override
                    public void onDefinitionChanged(VideoDefinition definition, String videoUrl) {

                    }
                })
                .setCommonListener(new VideoCommonListener() {
                    @Override
                    public void onDurationChanged(int totalDuration, int curDuration) {

                    }

                    @Override
                    public void onVideoStatusChanged(int videoStatus, String msg) {
                        switch (videoStatus) {
                            case VideoStatus.STATE_ERROR:
                                Log.d(TAG, "视频播放错误:" + msg);
                                break;
                            case VideoStatus.STATE_IDLE:
                                Log.d(TAG, "视频播器放空闲状态");
                                break;
                            case VideoStatus.STATE_BUFFERING:
                                Log.d(TAG, "视频缓冲中");
                                break;
                            case VideoStatus.STATE_PLAYING:
                                Log.d(TAG, "视频播放中");
                                break;
                            case VideoStatus.STATE_ENDED:
                                Log.d(TAG, "视频播放结束");
                                break;
                        }
                    }

                    @Override
                    public void onSpeedChanged(float speed) {
                        Log.d(TAG, "倍数成功切换为:" + speed);
                    }
                })
                .setShowSpeedView(true)
                .setSpeedValue(new float[]{0.5f, 1.0f, 1.5f, 2.0f})
                .build(this, mSimpleVideoView);
        mSimpleVideoView.prepare(Constants.VIDEO_URL_M3U8);

        //        List<SrtData> srtList = new ArrayList<>();
        //        srtList.add(new SrtDataImpl(1000, 4000, "how are you"));
        //        srtList.add(new SrtDataImpl(6000, 8000, "i am fine"));
        //        srtList.add(new SrtDataImpl(10000, 18000, "and you, Constants.VIDEO_URL_1_SUPER fly baby om super "));
        //        mSimpleVideoView.setSrt(srtList);

        mSimpleVideoView.start();
    }

    @Override
    protected void onDestroy() {
        mSimpleVideoView.release();
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSimpleVideoView.stop();
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSimpleVideoView.updateScreenOritation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSimpleVideoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mSimpleVideoView.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSimpleVideoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = Utils.dp2px(this, 220);
            mSimpleVideoView.setLayoutParams(params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnShowTopBar:
                mSimpleVideoView.changeTopBarVisibile(true);
                break;
            case R.id.mBtnHideTopBar:
                mSimpleVideoView.changeTopBarVisibile(false);
                break;
            case R.id.mBtnShowBottomBar:
                mSimpleVideoView.changeBottomBarVisibile(true);
                break;
            case R.id.mBtnHideBottomBar:
                mSimpleVideoView.changeBottomBarVisibile(false);
                break;
        }
    }

}
