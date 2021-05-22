package com.milo.libbase.demo.ui.screenrecord;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.milo.libbase.demo.databinding.ActivityScreenRecordBinding;
import com.milo.libbase.framework.OriginJump;
import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.utils.Utils;
import com.milo.libbase.utils.filefactory.FileFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 5/22/21
 */
public class ScreenRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String tag = ScreenRecordActivity.class.getSimpleName();

    private ActivityScreenRecordBinding binding;

    MediaProjectionManager mProjectionManager;

    public static OriginJump createJump(Context context) {
        return new OriginJump(context, ScreenRecordActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreenRecordBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.btnStart.setOnClickListener(this);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.videoView.getLayoutParams();
        params.height = (int)(Utils.getScreenWidth(this) * 9.0 / 16.0);
        binding.videoView.setLayoutParams(params);

    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnStart) {
            mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            Intent captureIntent = mProjectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, 100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaProjection mediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection == null) {
            LogUtils.e(tag, "media projection is null");
            return;
        }
        String filePath = FileFactory.COMMON.getFilePath(System.currentTimeMillis() + ".mp4");
        //        mediaRecord = new MediaRecordService(displayWidth, displayHeight, 6000000, 1,
        //                mediaProjection, file.getAbsolutePath());
        //        mediaRecord.start();

        final ScreenRecordService recordService = new ScreenRecordService(100, 100, 6000000, 1,
                mediaProjection, filePath);
        recordService.start();
        LogUtils.d(tag, "开始录制");
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        recordService.quit();
                        LogUtils.d(tag, "结束录制");
                    }
                });
    }


}
