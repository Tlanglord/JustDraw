package com.lht.justdraw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.lht.justdraw.R;
import com.lht.justdraw.util.ScreenUtil;

/**
 * Created by lht on 16/8/30.
 */
public class NativeProView extends View {

    int mWidth, mHeight;
    Context mContext;
    Paint mPaint;

    Bitmap mBitmap = null;
    boolean bDrawFinished = false;

    int mCount;
    OnDrawFinishedListener onDrawFinishedListener;

    public NativeProView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public NativeProView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWidth = manager.getDefaultDisplay().getWidth();
        mHeight = manager.getDefaultDisplay().getHeight()
                - ScreenUtil.getStatusBarHeight(mContext)
                - ScreenUtil.getNavBarHeight(mContext) - 17;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public void setCount(int count) {
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(mBitmap);

        int i = count;
        Path path;
        while (i-- > 0) {
            float x = (float)Math.random() * mWidth;
            float y = (float)Math.random() * mHeight;

            path = new Path();
            path.moveTo(x, y);
            path.lineTo(x + 300, y);
            path.addRect(x, y, x + 20, y + 20, Path.Direction.CW);

            RectF rectF = new RectF(x - 2, y - 2, x + 2, y + 2);
            path.addArc(rectF, 0, (float)(360 / Math.PI * 180));

            canvas.drawPath(path, mPaint);
        }
        bDrawFinished = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long start = System.currentTimeMillis();
        if (bDrawFinished) {
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            mBitmap.recycle();
            bDrawFinished = false;
        }
        long end = System.currentTimeMillis();

        onDrawFinishedListener.onDrawFinished(
                String.format(
                        getResources().getString(R.string.format_time), end - start + ""));
    }

    public void setOnDrawFinishedListener(OnDrawFinishedListener onDrawFinishedListener) {
        this.onDrawFinishedListener = onDrawFinishedListener;
    }
}
