package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.milo.libbase.R;
import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.utils.Utils;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/4/15
 */
public class SimpleLoadingView implements LoadingView {

    private Context mContext;

    public ImageView imgProgress;

    public SimpleLoadingView(Context context) {
        this.mContext = context;

        init();
    }

    @Override
    public View getLoadingView() {
        return imgProgress;
    }

    @Override
    public void showLoadingView(ViewGroup parent) {
        try {
            if(imgProgress.getParent() == null){
                parent.addView(imgProgress);
                CardView.LayoutParams params = (FrameLayout.LayoutParams)imgProgress.getLayoutParams();
                params.width = Utils.dp2px(mContext, 50);
                params.height = Utils.dp2px(mContext, 50);
                params.gravity = Gravity.CENTER;
                imgProgress.setLayoutParams(params);
            }

            imgProgress.setVisibility(View.VISIBLE);
            imgProgress.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.libbase_video_progress);
            imgProgress.startAnimation(animation);

        } catch (Exception e) {
            LogUtils.e(getClass().getSimpleName(), "showProgress-error: " + e.getMessage());
        }
    }

    @Override
    public void hideLoadingView(ViewGroup parent) {
        try {
            imgProgress.clearAnimation();

            if(imgProgress.getParent() == parent){
                parent.removeView(imgProgress);
            } else {
                imgProgress.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e(getClass().getSimpleName(), "hideProgress-error: " + e.getMessage());
        }
    }

    protected void init() {
        imgProgress = (ImageView) (LayoutInflater.from(mContext).inflate(R.layout.libbase_vh_simple_loadingview, null));
    }

}
