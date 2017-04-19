package com.cloudy.linglingbang.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.cloudy.linglingbang.R;
import com.cloudy.linglingbang.app.util.DensityUtil;

/**
 * Created by yongfei on 2017/4/18 0018.
 * 带badge标记的TextView
 */

public class BadgeView extends View {
    private final int HIDE_BADGE = 0;
    private final int POINT_BADGE = 1;
    private final int PIC_RES_BADGE = 2;

    private int drawBadgeType; // 绘制的类型
    private int drawBadgeCacheType; // 绘制的类型
    private float badgeOffsetX;
    private float badgeOffsetY;

    private Bitmap badgePicture;
    private String badgePicPath;
    private Paint mPaint;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private String mBadgeText;
    protected RectF mBadgeTextRect;
    protected TextPaint mBadgeTextPaint;
    protected Paint.FontMetrics mBadgeTextFontMetrics;
    private float textSize;
    private int textColor;
    private int defaultPaintColor;
    private int defaultPointSize; // 圆点标记的默认半径 默认为0，通过获取宽高最小值的1/4获取

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBadgeTextRect = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        drawBadgeCacheType = HIDE_BADGE;

        mBadgeTextPaint = new TextPaint();
        mBadgeTextPaint.setAntiAlias(true);
        mBadgeTextPaint.setSubpixelText(true);
        mBadgeTextPaint.setFakeBoldText(true);
        mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        defaultPaintColor = R.color.red;
        defaultPointSize = 0;
    }

    public void bindView(View targetView){
        if (targetView == null){
            throw new IllegalStateException("targetView can not be null");
        }
        // remove from this view's parent,to prevent repeat call this function
        if (getParent() != null){
            ((ViewGroup)getParent()).removeView(this);
        }

        // 获取targetView的viewParent
        ViewParent targetParent = targetView.getParent();
        if (targetParent != null && targetParent instanceof ViewGroup){
            ViewGroup targetContainer = (ViewGroup) targetParent;
            int index = targetContainer.indexOfChild(targetView);
            ViewGroup.LayoutParams params = targetView.getLayoutParams();

            targetContainer.removeView(targetView);

            // add this view to targetContainer
            BadgeContainer badgeContainer = new BadgeContainer(getContext());
            targetContainer.addView(badgeContainer, index, params);
            badgeContainer.addView(targetView, params);
            badgeContainer.addView(this);
        } else {
            throw(new IllegalStateException("targetParent should be viewGroup"));
        }
    }

    /**
     * 设置显示的图片资源
     *
     * @param res res
     */
    public BadgeView showImageRes(int res) {
        drawBadgeCacheType = PIC_RES_BADGE;
        if (res == -1) {
            badgePicture = BitmapFactory.decodeResource(getResources(), R.drawable.ee_7);
        } else {
            badgePicture = BitmapFactory.decodeResource(getResources(), res);
        }
        return this;
    }

    /**
     * 显示图片标记
     * @param resPath image file path
     */
    public BadgeView showImageRes(String resPath) {
        drawBadgeCacheType = PIC_RES_BADGE;
        badgePicPath = resPath;
        return this;
    }

    /**
     * 显示圆点标记
     */
    public BadgeView showBadgePoint() {
        return showBadgePoint(defaultPointSize);
    }

    public BadgeView showBadgePoint(int badgeSize) {
        return showBadgePoint(badgeSize, defaultPaintColor);
    }

    public BadgeView showBadgePoint(int badgeSize,int badgeColor) {
        this.defaultPointSize = badgeSize;
        mPaint.setColor(getResources().getColor(badgeColor));
        drawBadgeCacheType = POINT_BADGE;
        return this;
    }

    /**
     * 设置x，y的偏移量
     * @param badgeOffsetX
     * @param badgeOffsetY
     * @param isDP         传递值是否是dp
     */
    public BadgeView setBadgeOffset(int badgeOffsetX, int badgeOffsetY, boolean isDP) {
        if (isDP) {
            this.badgeOffsetX = DensityUtil.dip2px(getContext(), badgeOffsetX);
            this.badgeOffsetY = DensityUtil.dip2px(getContext(), badgeOffsetY);
        } else {
            this.badgeOffsetX = badgeOffsetX;
            this.badgeOffsetY = badgeOffsetY;
        }
        return this;
    }

    public void show() {
        drawBadgeType = drawBadgeCacheType;
        invalidate();
    }

    /**
     * 返回是否正在显示的状态
     *
     * @return
     */
    public boolean isShow() {
        return drawBadgeType != HIDE_BADGE;
    }

    /**
     * 隐藏当前的标记
     */
    public void hide() {
        drawBadgeCacheType = drawBadgeType;
        drawBadgeType = HIDE_BADGE;
        invalidate();
    }

    private void measureText() {
        mBadgeTextRect.left = 0;
        mBadgeTextRect.top = 0;
        if (TextUtils.isEmpty(mBadgeText)) {
            mBadgeTextRect.right = 0;
            mBadgeTextRect.bottom = 0;
        } else {
            mBadgeTextPaint.setTextSize(textSize);
            mBadgeTextRect.right = mBadgeTextPaint.measureText(mBadgeText);
            mBadgeTextFontMetrics = mBadgeTextPaint.getFontMetrics();
            mBadgeTextRect.bottom = mBadgeTextFontMetrics.descent - mBadgeTextFontMetrics.ascent;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
        measureText();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch (drawBadgeType) {
            case POINT_BADGE:
                drawPointWithColor(canvas);
                break;
            case PIC_RES_BADGE:
                drawImageBadge(canvas);
                break;
        }
    }

    private void drawImageBadge(Canvas canvas) {
        int textCenterWidth = mMeasuredWidth / 2;
        int textCenterHeight = mMeasuredHeight / 2;
        int minViewSize = Math.min(textCenterWidth, textCenterHeight);

        if (badgePicture == null && !TextUtils.isEmpty(badgePicPath)) {
            badgePicture = scaleBitmapFromFile(badgePicPath, minViewSize);
        }

        if (badgePicture == null) {
            return;
        }
        int pictureWidth = badgePicture.getWidth();
        int pictureHeight = badgePicture.getHeight();

        int maxImageSize = Math.max(pictureWidth, pictureHeight);

        // 如果图片宽高最大值大于当前view宽高1/2最小值，压缩图片
        if (maxImageSize >= minViewSize) {
            badgePicture = scaleBitmap(badgePicture, minViewSize / 2, minViewSize / 2);
            pictureWidth = badgePicture.getWidth();
            pictureHeight = badgePicture.getHeight();
            maxImageSize = Math.max(pictureWidth, pictureHeight);
        }

        float left = mMeasuredWidth - maxImageSize + badgeOffsetX;
        float top = badgeOffsetY;
        canvas.drawBitmap(badgePicture, left, top, mPaint);
    }

    private void drawPointWithColor(Canvas canvas) {
        // 计算未读标记中心点位置和半径值 默认在中间位置
        int textCenterWidth = mMeasuredWidth / 2;
        int textCenterHeight = mMeasuredHeight / 2;
        int offset = textCenterWidth > textCenterHeight ? textCenterHeight : textCenterWidth;

        float cx = mMeasuredWidth - offset / 2 + badgeOffsetX;
        float cy = offset / 2 + badgeOffsetY;

        float radius = defaultPointSize;
        if (radius == 0) {
            if (textCenterWidth > textCenterHeight) {
                radius = textCenterHeight / 4;
            } else {
                radius = textCenterWidth / 4;
            }
        }
        canvas.drawCircle(cx, cy, radius, mPaint);
    }

    private Bitmap scaleBitmapFromFile(String imgPath, int imgScaleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        if (bitmap == null) {
            return null;
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        // 计算缩放比
        int scale;
        if (imgScaleSize < realWidth || imgScaleSize < realHeight) {
            scale = (int) (realWidth / imgScaleSize + 1);
        } else {
            scale = (int) (imgScaleSize / realWidth + 1);
        }
        if (scale <= 0) { // 默认不缩放
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    private Bitmap scaleBitmap(Bitmap bigBitmap, float newWidth, float newHeight) {
        float width = bigBitmap.getWidth();
        float height = bigBitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        if (scaleWidth > 1){
            scaleWidth = 1;
        }
        if (scaleHeight > 1){
            scaleHeight = 1;
        }
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bigBitmap, 0, 0, (int) width,
                (int) height, matrix, true);
    }

    private class BadgeContainer extends ViewGroup {

        public BadgeContainer(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            View targetView = null, badgeView = null;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!(child instanceof BadgeView)) {
                    targetView = child;
                } else {
                    badgeView = child;
                }
            }
            if (targetView == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                targetView.measure(widthMeasureSpec, heightMeasureSpec);
                if (badgeView != null) {
                    badgeView.measure(MeasureSpec.makeMeasureSpec(targetView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(targetView.getMeasuredHeight(), MeasureSpec.EXACTLY));
                }
                setMeasuredDimension(targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
            }
        }
    }
}
