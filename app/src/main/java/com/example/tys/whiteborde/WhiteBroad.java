package com.example.tys.whiteborde;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class WhiteBroad extends View {

    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private TextPaint textPain;
    private Paint mPaint;
    private float mX;// start point,
    private float mY;
    private int bgColor = Color.GRAY;


    public WhiteBroad(Context context) {
        this(context, null);
    }

    public WhiteBroad(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhiteBroad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        setFocusable(true);
        setFocusableInTouchMode(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);


        textPain = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPain.setHinting(Paint.HINTING_ON);
        textPain.setColor(Color.BLACK);
        textPain.setTextSize(66);
        textPain.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/hwxk.ttf"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgColor);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
//        canvas.drawPath(mPath, mPaint);
        canvas.drawText("sign", 33, 66, textPain);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(mX, mY, x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp(x, y);
                invalidate();
                break;
        }
        return true;
    }

    private void touchMove(float oldX, float oldY, float newX, float newY) {
        mPath.reset();
        mPath.moveTo(oldX, oldY);
        mPath.quadTo(oldX, oldY, newX, newY);
        mX = newX;
        mY = newY;
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    private void touchUp(float x, float y) {
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    private void touchStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    public void clean() {
        mCanvas.drawColor(bgColor);
        mPath.reset();
        invalidate();
    }

    public boolean save(@NonNull String saveDir, @NonNull String saveName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        File outFile = new File(saveDir, saveName);
        try {
            outFile.deleteOnExit();
            FileOutputStream outPut = new FileOutputStream(outFile);
            out.writeTo(outPut);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}





