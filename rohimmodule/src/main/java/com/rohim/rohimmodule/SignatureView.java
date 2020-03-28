package com.rohim.rohimmodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SignatureView extends View {

    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int bgColor;

    private float curX, curY;
    private boolean isDragged = false;

    private static final int TOUCH_TOLERANCE = 4;
    private static final int STROKE_WIDTH = 2;

    public SignatureView(Context context) {
        super(context);
        init();
    }
    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public SignatureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setFocusable(true);
        bgColor = Color.WHITE;
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(bgColor ^ 0x00FFFFFF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);
    }

    public void setSigColor(int color) {
        mPaint.setColor(color);
    }

    public void setSigColor(int a, int r, int g, int b) {
        mPaint.setARGB(a, r, g, b);
    }

    public void clearSignature() {
        if (mCanvas != null) {
            mCanvas.drawColor(bgColor);
            mPath.reset();
            invalidate();
        }
    }

    @Deprecated
    public void clearSig() {
        clearSignature();
    }

    public Bitmap getImage() {
        return this.mBitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int bitW = mBitmap != null ? mBitmap.getWidth() : 0;
        int bitH = mBitmap != null ? mBitmap.getWidth() : 0;
        if (bitW >= w && bitH >= h) {
            return;
        }

        if (bitW < w) bitW = w;
        if (bitH < h) bitH = h;

        // create a new bitmap and canvas for the new size
        Bitmap newBitmap = Bitmap.createBitmap(bitW, bitH, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);

        if (mBitmap != null) {	// already have a bitmap
            newCanvas.drawBitmap(mBitmap, 0, 0, null);	// redraw it onto the new bitmap
        } else {				// no path yet
            newCanvas.drawColor(bgColor);
        }
        // Replace the old bitmap and canvas with the new one
        mBitmap = newBitmap;
        mCanvas = newCanvas;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }
        invalidate();
        return true;
    }

    private void touchDown(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        curX = x;
        curY = y;
        isDragged = false;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - curX);
        float dy = Math.abs(y - curY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(curX, curY, (x + curX)/2, (y + curY)/2);
            curX = x;
            curY = y;
            isDragged = true;
        }
    }

    private void touchUp() {
        if (isDragged) {
            mPath.lineTo(curX, curY);
        } else {
            mPath.lineTo(curX+2, curY+2);
        }
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }
}
