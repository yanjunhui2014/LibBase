package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.milo.libbase.R;
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
public class SimplePlayIconControl implements PlayIconControl {

    private Context   mContext;
    public  ImageView imgIcon;

    public SimplePlayIconControl(Context context) {
        this.mContext = context;

        init();
    }

    @Override
    public void showPlayIcon(ViewGroup parent, int type) {
        if (imgIcon.getParent() == null) {
            parent.addView(imgIcon);
            CardView.LayoutParams params = (FrameLayout.LayoutParams) imgIcon.getLayoutParams();
            params.width = Utils.dp2px(mContext, 50);
            params.height = Utils.dp2px(mContext, 50);
            params.gravity = Gravity.CENTER;
            imgIcon.setLayoutParams(params);
        }

        imgIcon.setVisibility(View.VISIBLE);
        if (type == PlayIconControl.TYPE_PAUSE) {
            imgIcon.setImageResource(R.mipmap.libbase_ic_suspend_big);
        } else if (type == PlayIconControl.TYPE_PLAY) {
            imgIcon.setImageResource(R.mipmap.libbase_ic_play_big);
        }
    }

    @Override
    public void hidePlayIcon(ViewGroup parent) {
        imgIcon.setVisibility(View.GONE);
    }

    @Override
    public View getPlayIconView() {
        return imgIcon;
    }

    protected void init() {
        imgIcon = (ImageView) (LayoutInflater.from(mContext).inflate(R.layout.libbase_vh_simple_playicon, null));
    }

}
