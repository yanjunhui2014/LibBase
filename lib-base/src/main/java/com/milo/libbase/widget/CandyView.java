package com.milo.libbase.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.milo.libbase.R;

/**
 * Title：糖果币视图
 * Describe：
 * Remark：保证设置的糖果币数居中显示
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/1/26
 */
public class CandyView extends View {
    private TextPaint mTextPaint;

    private int mBaseX       = 0;
    private int mCenterX     = 0;
    private int mCandyNumber = 999;

    private float mTextSize = 40.0f;
    @ColorRes
    private int mTextColor = R.color.libbase_white;

    public CandyView(Context context) {
        super(context);
    }

    public CandyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CandyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeW = MeasureSpec.getMode(widthMeasureSpec);
        if (specModeW == MeasureSpec.AT_MOST || specModeW == MeasureSpec.UNSPECIFIED) {
            throw new IllegalArgumentException("使用该view时必须指定宽度");
        }
        int exactlyWidth = MeasureSpec.getSize(widthMeasureSpec);
        int exactlyHeight = (int) (exactlyWidth * 96.0 / 216.0);
        super.onMeasure(MeasureSpec.makeMeasureSpec(exactlyWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(exactlyHeight, MeasureSpec.EXACTLY));
        mBaseX = (int) (getMeasuredWidth() * 0.45);
        mCenterX = (int) (getMeasuredWidth() * 0.45);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setBackgroundResource(R.mipmap.libbase_ic_candy);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTextPaint == null) {
            initTextPaint();
        }

        String text = String.valueOf(mCandyNumber);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = Math.abs(fontMetrics.top) + fontMetrics.bottom;
        float y = getMeasuredHeight() / 2.0f + textHeight / 2.0f - fontMetrics.bottom;

        canvas.drawText(text, mBaseX + (mCenterX - mTextPaint.measureText(text)) / 2.0f, y, mTextPaint);
    }

    private void initTextPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAlpha(255);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), mTextColor));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    public void setCandyNumber(int candyNumber) {
        this.mCandyNumber = candyNumber;
        invalidate();
    }

    /**
     * Set the paint's text size. This value must be > 0
     *
     * @param textSize set the paint's text size in pixel units.
     */
    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    /**
     * Set the paint's color. Note that the color is an int containing alpha
     * as well as r,g,b. This 32bit value is not premultiplied, meaning that
     * its alpha can be any value, regardless of the values of r,g,b.
     * See the Color class for more details.
     *
     * @param color The new color (including alpha) to set in the paint.
     */
    public void setTextColor(@ColorRes int color) {
        this.mTextColor = color;
    }

}
