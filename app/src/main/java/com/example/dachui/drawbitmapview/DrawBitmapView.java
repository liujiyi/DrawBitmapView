package com.example.dachui.drawbitmapview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>描述：移动的view（目前只支持bitmap）</p>
 * 实现图片从左到右缓慢加载显示
 * 作者： liujiyi<br>
 */
public class DrawBitmapView extends View {
    private Resources mResources;
    private Paint mBitPaint;
    private Bitmap mBitmap;
    private int mBitmapUrl;
    private Rect mSrcRect, mDestRect;

    // view 的宽高
    private int mTotalWidth, mTotalHeight;
    private int mBitWidth, mBitHeight;

    public DrawBitmapView(Context context) {
        this(context, null);
    }

    public DrawBitmapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawBitmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BitmapAddress, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.BitmapAddress_address:
                    mBitmapUrl = a.getResourceId(R.styleable.BitmapAddress_address, 0);
                    break;
            }
        }
        a.recycle();
        if (mBitmapUrl == 0) return;
        mResources = getResources();
        initBitmap(mBitmapUrl);
        initPaint();
    }

    private void initPaint() {
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
    }

    private void initBitmap(int url) {
        mBitmap = BitmapFactory.decodeResource(getResources(), url);
        mBitWidth = mBitmap.getWidth();
        mBitHeight = mBitmap.getHeight();
        mSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
        mDestRect = new Rect(0, 0, mBitWidth, mBitHeight);
    }

    private void initBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mBitWidth = mBitmap.getWidth();
        mBitHeight = mBitmap.getHeight();
        mSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
        mDestRect = new Rect(0, 0, mBitWidth, mBitHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, mBitPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
    }

    /**
     * 实现图片从左到右缓慢加载显示
     * @param duration
     * @param interpokator
     */
    public void startMove(long duration, TimeInterpolator interpokator) {
        // 使用ValueAnimator创建一个过程
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpokator);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mListener != null) {
                    mListener.endMove();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                // 不断重新计算上下左右位置
                float fraction = (Float) animator.getAnimatedValue();
                if (mSrcRect == null) {
                    mSrcRect = new Rect(0, 0, mBitWidth,
                            mBitHeight);
                }
                mSrcRect.right = (int) (fraction * mBitWidth);
                mDestRect.right = (int) (fraction * mBitWidth);
                // 重绘
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    private OnAnimEndListener mListener;
    public void setOnAnimEndListener(OnAnimEndListener listener) {
        this.mListener = listener;
    }

    public interface OnAnimEndListener {
        void endMove();
    }
}
