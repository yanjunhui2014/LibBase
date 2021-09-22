package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.milo.libbase.AppConfig;
import com.milo.libbase.R;
import com.milo.libbase.utils.Utils;
import com.milo.libbase.utils.WeakHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Title：二代播放器基类
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/25
 */
public abstract class BaseVideoView<T> extends CardView implements IVideoView<T> {
    final int MSG_UPDATE_DURATION = 1000;
    final int MSG_HIDE_TOP_BAR = 1001;
    final int MSG_HIDE_BOTTOM_BAR = 1002;

    public RelativeLayout mLayoutContainer;

    protected String mVideoUrl;
    protected String mPicUrl;
    protected String mAudioUrl;

    protected float startX = 0;
    protected float startY = 0;
    protected boolean isLeftScreen = false;

    protected int volumeValue = -1;
    protected int brightValue = -1;
    protected int screenOritation;
    protected boolean interceptMove;
    protected long actionDownTime;

    protected VideoDefinition curVideoDefinition;

    public TextView mTvSrt;
    protected PercentVH mPercentVH;

    private List<? extends SrtData> mSrtDataList = new ArrayList<>();

    protected boolean showTopbar;
    protected boolean showBottomBar;
    protected long topBarHideDelay;
    protected long bottomBarHideDelay;
    protected boolean volumeEnable;
    protected boolean brightnessEnable;
    protected boolean videoLoop;
    protected boolean showSpeedView;
    protected float speedValue[];

    protected String videoTitle;

    protected TopBar topBar;
    protected BottomBar bottomBar;
    protected LoadingView loadingView;
    protected PlayIconControl playIconControl;

    protected TopBarListener topBarListener;
    protected BottomBarListener bottomBarListener;

    protected VideoCommonListener commonListener;

    private Map<VideoDefinition, VideoData> definitionMap;
    private DefinitionPopupWindow<? extends BaseVideoView> definitionPopupWinwdow;
    private DefinitionPopupWindow.DefinitionChangedListener definitionChangedListener;

    private SpeedPopupWindow speedPopupWindow;
    private SpeedPopupWindow.SpeedWindowListener speedWindowListener;

    protected WeakHandler mWeakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_DURATION:
                    if (getDuration() != 0) {
                        int duration = getDuration();
                        int curDuration = getCurDuration();
                        if (bottomBar != null) {
                            if (bottomBar.getSeekBar() != null) {
                                bottomBar.getSeekBar().setMax(duration);
                                bottomBar.getSeekBar().setProgress(curDuration);
                            }
                            bottomBar.getTotalTimeTextView().setText(Utils.millsToString(duration));
                            bottomBar.getCurTimeTextView().setText(Utils.millsToString(curDuration));
                        }
                        if (commonListener != null) {
                            commonListener.onDurationChanged(duration, curDuration);
                        }

                        //更新字幕
                        if (mSrtDataList != null) {
                            String srt = null;
                            for (int i = 0; i < mSrtDataList.size(); i++) {
                                SrtData itemData = mSrtDataList.get(i);
                                if (curDuration <= itemData.getEnd() && curDuration >= itemData.getStart()) {
                                    srt = itemData.getSrt();
                                    break;
                                }
                            }

                            if (srt != null) {
                                mTvSrt.setText(srt);
                            } else {
                                mTvSrt.setText("");
                            }
                        }
                    }
                    mWeakHandler.sendEmptyMessageDelayed(MSG_UPDATE_DURATION, 16);
                    break;
                case MSG_HIDE_TOP_BAR:
                    hideTopBar();
                    break;
                case MSG_HIDE_BOTTOM_BAR:
                    hideBottomBar();
                    break;
            }
            return false;
        }
    }) {
    };

    public BaseVideoView(Context context) {
        super(context);
    }

    public BaseVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepare(VideoDefinition definition) {
        prepare(definition, null);
    }

    @Override
    public void prepare(VideoDefinition definition, String picUrl) {
        prepare(definition, picUrl, null);
    }

    @Override
    public void prepare(VideoDefinition definition, String picUrl, String audioUrl) {
        try {
            this.curVideoDefinition = definition;
            bottomBar.getDefinitionTextView().setText(getDefinitionText(definition));
            VideoData videoData = definitionMap.get(definition);
            prepare(videoData.mVideoUrl, picUrl, audioUrl);
        } catch (Exception e) {
            Toast.makeText(getContext(), String.format("%s视频播放错误", getDefinitionText(definition)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void prepare(String url) {
        this.prepare(url, null);
    }

    @Override
    public void prepare(String url, String picUrl) {
        this.prepare(url, picUrl, null);
    }

    @Override
    public void prepare(String url, String picUrl, String audioUrl) {
        this.mVideoUrl = url;
        this.mPicUrl = picUrl;
        this.mAudioUrl = audioUrl;
    }

    @Override
    public void release() {
        if (mWeakHandler != null) {
            mWeakHandler.removeCallbacksAndMessages(null);
            mWeakHandler = null;
        }
    }

    @Override
    public TopBar getTopBar() {
        return topBar;
    }

    @Override
    public BottomBar getBottomBar() {
        return bottomBar;
    }

    @Override
    public LoadingView getLoadingView() {
        return loadingView;
    }

    @Override
    public void showTopBar(long hideDelay) {
        if (topBar == null) {
            return;
        }

        if (showTopbar) {
            topBar.getTopBarView().setVisibility(View.VISIBLE);
            mWeakHandler.removeMessages(MSG_HIDE_TOP_BAR);
            mWeakHandler.sendEmptyMessageDelayed(MSG_HIDE_TOP_BAR, hideDelay);
        }
    }

    @Override
    public void showBottomBar(long hideDelay) {
        if (bottomBar == null) {
            return;
        }
        if (showBottomBar) {
            bottomBar.getBottomBarView().setVisibility(View.VISIBLE);
            mWeakHandler.removeMessages(MSG_HIDE_BOTTOM_BAR);
            mWeakHandler.sendEmptyMessageDelayed(MSG_HIDE_BOTTOM_BAR, hideDelay);
        }
    }

    @Override
    public void hideTopBar() {
        if (topBar != null && topBar.getTopBarView() != null) {
            topBar.getTopBarView().setVisibility(View.GONE);
        }
    }

    @Override
    public void hideBottomBar() {
        if (bottomBar != null && bottomBar.getBottomBarView() != null) {
            bottomBar.getBottomBarView().setVisibility(View.GONE);
        }
    }

    @Override
    public void changeTopBarVisibile(boolean visibile) {
        if (topBar == null || topBar.getTopBarView() == null) {
            return;
        }
        if (visibile) {
            if (topBar.getTopBarView().getParent() == null) {
                initTopBar();
            }
        } else {
            if (topBar.getTopBarView().getParent() != null) {
                ((ViewGroup) topBar.getTopBarView().getParent()).removeView(topBar.getTopBarView());
            }
        }
    }

    @Override
    public void changeBottomBarVisibile(boolean visibile) {
        if (bottomBar == null || bottomBar.getBottomBarView() == null) {
            return;
        }
        if (visibile) {
            if (bottomBar.getBottomBarView().getParent() == null) {
                initBottomBar();
            }
        } else {
            if (bottomBar.getBottomBarView().getParent() != null) {
                ((ViewGroup) bottomBar.getBottomBarView().getParent()).removeView(bottomBar.getBottomBarView());
            }
        }
    }

    @Override
    public boolean enableProxy() {
        if (TextUtils.isEmpty(mVideoUrl) || !mVideoUrl.startsWith("http")) {
            return false;
        }
        return true;
    }

    @Override
    public String getProxyUrl(String url) {
        String proxyUrl = Utils.getProxyUrl(AppConfig.app, url);
        return proxyUrl;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionDownTime = System.currentTimeMillis();
                screenOritation = getContext().getResources().getConfiguration().orientation;

                startX = event.getX();
                startY = event.getY();
                if (volumeEnable) {
                    volumeValue = VolumeHelper.getInstance(getContext()).get100CurrentVolume();
                }
                if (brightnessEnable) {
                    brightValue = BrightnessHelper.getInstance().getWindowBrightness(getContext());
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                int moveY = (int) (event.getY() - startY);
                if (Math.abs(moveY) <= Utils.dp2px(getContext(), 5)) {
                    return super.onTouchEvent(event);
                }

                interceptMove = true;

                if (isLeftHalfScreen(event.getX()) && brightnessEnable) {
                    isLeftScreen = true;
                    int brightnessValue = brightValue - moveY / 3;
                    BrightnessHelper.getInstance().setWindowBrightness(getContext(), brightnessValue);
                    if (mPercentVH == null) {
                        mPercentVH = new PercentVH(getContext());
                    }
                    mPercentVH.setType(PercentVH.Type.BRIGHTNESS);
                    mPercentVH.attach(this);
                    mPercentVH.setPercent(BrightnessHelper.getInstance().get100CurrentBrightness(getContext()));
                    return true;
                } else if (!isLeftScreen && volumeEnable) {
                    VolumeHelper.getInstance(getContext()).setVoice100(volumeValue - moveY / 9);
                    if (mPercentVH == null) {
                        mPercentVH = new PercentVH(getContext());
                    }
                    mPercentVH.setType(PercentVH.Type.VOLUME);
                    mPercentVH.attach(this);
                    mPercentVH.setPercent(VolumeHelper.getInstance(getContext()).get100CurrentVolume());
                    return true;
                }
            }
            break;
            case MotionEvent.ACTION_UP:
                isLeftScreen = false;
                volumeValue = VolumeHelper.getInstance(getContext()).get100CurrentVolume();
                float endX = event.getX();
                float endY = event.getY();

                if (mPercentVH != null) {
                    mPercentVH.detach(this);
                }

                if (!interceptMove) {
                    if (System.currentTimeMillis() - actionDownTime <= 1000) {
                        simulationClick();
                    }
                }
                interceptMove = false;

                if (Math.abs(endX - startX) >= Utils.dp2px(getContext(), 50)) {
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                interceptMove = false;
                break;
        }
        return true;
    }

    public void setSrt(List<? extends SrtData> srtDataList) {
        this.mSrtDataList = srtDataList;
        if (mSrtDataList != null) {
            mTvSrt.setVisibility(View.VISIBLE);
        }
    }

    public List<? extends SrtData> getSrt() {
        return this.mSrtDataList;
    }

    public void clearSrt() {
        this.mSrtDataList.clear();
        mTvSrt.setVisibility(View.GONE);
    }

    public void updateScreenOritation(int screenOritation) {
        if (screenOritation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            if (bottomBar != null && bottomBar.getWholeScreenView() != null) {
                bottomBar.getWholeScreenView().setImageResource(bottomBar.getWholeScreenResIds()[1]);
                bottomBar.getWholeScreenView().setTag(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (screenOritation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            if (bottomBar != null && bottomBar.getWholeScreenView() != null) {
                bottomBar.getWholeScreenView().setImageResource(bottomBar.getWholeScreenResIds()[0]);
                bottomBar.getWholeScreenView().setTag(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    protected void init() {
        mLayoutContainer = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.libbase_base_video_view, null);
        addView(mLayoutContainer);

        if (getCoreView() != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mLayoutContainer.addView((View) getCoreView(), params);
        }
        if (mTvSrt == null) {
            mTvSrt = new TextView(getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.leftMargin = Utils.dp2px(getContext(), 80);
            params.rightMargin = Utils.dp2px(getContext(), 80);
            params.bottomMargin = Utils.dp2px(getContext(), 18);
            mTvSrt.setTextColor(Color.WHITE);
            mTvSrt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mTvSrt.setGravity(Gravity.CENTER);
            mLayoutContainer.addView(mTvSrt, params);
        }
        if (topBar != null && showTopbar) {
            initTopBar();
        }
        if (bottomBar != null && showBottomBar) {
            initBottomBar();
        }

        mWeakHandler.sendEmptyMessage(MSG_UPDATE_DURATION);
    }

    protected void initTopBar() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLayoutContainer.addView(topBar.getTopBarView(), params);
        if (topBarListener != null) {
            topBar.getBackView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    topBarListener.onBackClick(v);
                }
            });
            topBar.getMenuView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    topBarListener.onMenuClick(v);
                }
            });
        }
        if (videoTitle != null) {
            topBar.getTitleTextView().setText(videoTitle);
        }
    }

    protected void initBottomBar() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLayoutContainer.addView(bottomBar.getBottomBarView(), params);

        bottomBar.getPlayView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying()) {
                    stop();
                } else {
                    start();
                }
            }
        });

        bottomBar.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            boolean isPlayingWhenStartTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlayingWhenStartTouch = isPlaying();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                seek(progress);
                if (isPlayingWhenStartTouch) {
                    start();
                }
            }
        });

        if (bottomBarListener != null && bottomBar.getWholeScreenView() != null) {
            bottomBar.getWholeScreenView().setTag(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            bottomBar.getWholeScreenView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = bottomBar.getWholeScreenView().getTag();
                    if (tag != null && ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == (int) tag) {
                        bottomBar.getWholeScreenView().setTag(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        bottomBar.getWholeScreenView().setImageResource(bottomBar.getWholeScreenResIds()[1]);
                        bottomBarListener.onWholeScreenClick(v, BottomBarListener.SCALE_TYPE_BIG);
                    } else {
                        bottomBar.getWholeScreenView().setTag(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        bottomBar.getWholeScreenView().setImageResource(bottomBar.getWholeScreenResIds()[0]);
                        bottomBarListener.onWholeScreenClick(v, BottomBarListener.SCALE_TYPE_SMALL);
                    }
                }
            });
        }

        if (definitionMap != null && bottomBar.getDefinitionTextView() != null) {
            bottomBar.getDefinitionTextView().setVisibility(View.VISIBLE);
            bottomBar.getDefinitionTextView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDefinitionPopupWindow(new DefinitionPopupWindow.DefinitionChangedListener() {
                        @Override
                        public void onDefiniChanged(VideoDefinition videoDefinition, String videoUrl) {
                            if (definitionChangedListener != null) {
                                definitionChangedListener.onDefiniChanged(videoDefinition, videoUrl);
                            }

                            final int curDuration = getCurDuration();
                            stop();
                            prepare(videoDefinition);
                            seek(curDuration);

                            if (isPlaying()) {
                                start();
                            }
                        }
                    });
                }
            });
        }

        if (showSpeedView && supportSpeed()) {
            bottomBar.getSpeedTextView().setVisibility(View.VISIBLE);
            bottomBar.getSpeedTextView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSpeedPopupWindow();
                }
            });
        }
    }

    private boolean isLeftHalfScreen(float x) {
        return getWidth() / 2 > (x - getX());
    }

    private void simulationClick() {
        if (topBar != null) {
            if (topBar.getTopBarView().getVisibility() == View.VISIBLE) {
                mWeakHandler.removeMessages(MSG_HIDE_TOP_BAR);
                topBar.getTopBarView().setVisibility(View.GONE);
            } else {
                showTopBar(topBarHideDelay);
            }
        }
        if (bottomBar != null) {
            if (bottomBar.getBottomBarView().getVisibility() == View.VISIBLE) {
                mWeakHandler.removeMessages(MSG_HIDE_BOTTOM_BAR);
                bottomBar.getBottomBarView().setVisibility(View.GONE);
            } else {
                showBottomBar(bottomBarHideDelay);
            }
        }
    }

    public String getDefinitionText(VideoDefinition videoDefinition) {
        if (videoDefinition == VideoDefinition.STANDARD) {
            return "标清";
        } else if (videoDefinition == VideoDefinition.HEIGHT) {
            return "高清";
        } else {
            return "超清";
        }
    }

    private void showDefinitionPopupWindow(DefinitionPopupWindow.DefinitionChangedListener definitionChangedListener) {
        if (definitionPopupWinwdow == null) {
            definitionPopupWinwdow = new DefinitionPopupWindow<BaseVideoView>(getContext(), this, definitionMap, definitionChangedListener);
        }

        int[] location = new int[2];
        this.getLocationOnScreen(location);
        definitionPopupWinwdow.mVideoHeightInPortrait = this.getHeight();
        definitionPopupWinwdow.setScreenOrientation(getResources().getConfiguration().orientation);
        definitionPopupWinwdow.setCurrentVideoDefinition(curVideoDefinition);

        definitionPopupWinwdow.showAtLocation(this, Gravity.RIGHT | Gravity.TOP, 0, location[1]);
        hideTopBar();
        hideBottomBar();
    }

    private void showSpeedPopupWindow() {
        if (speedPopupWindow == null) {
            speedPopupWindow = new SpeedPopupWindow(getContext(), new SpeedPopupWindow.SpeedWindowListener() {
                @Override
                public void onSpeedClick(float speed) {
                    if (bottomBar != null && bottomBar.getSpeedTextView() != null) {
                        if (!supportSpeed()) {
                            Toast.makeText(getContext(), "此播放器暂不支持倍速播放", Toast.LENGTH_SHORT).show();
                        } else {
                            if (setSpeed(speed)) {
                                bottomBar.getSpeedTextView().setText(String.format("%sx", speed));
                            } else {
                                Toast.makeText(getContext(), String.format("%s倍速切换失败", speed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (commonListener != null) {
                        commonListener.onSpeedChanged(speed);
                    }
                }
            });
            speedPopupWindow.setSpeedValues(speedValue);
        }

        int[] location = new int[2];
        this.getLocationOnScreen(location);
        speedPopupWindow.mVideoHeightInPortrait = this.getHeight();
        speedPopupWindow.setScreenOrientation(getResources().getConfiguration().orientation);

        speedPopupWindow.showAtLocation(this, Gravity.RIGHT | Gravity.TOP, 0, location[1]);
        hideTopBar();
        hideBottomBar();
    }

}
