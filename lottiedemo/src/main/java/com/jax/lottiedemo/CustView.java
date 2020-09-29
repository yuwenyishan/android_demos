package com.jax.lottiedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * @author 陈双龙
 * @date 2020/5/9
 * @description 描述：
 */
public class CustView extends View {
    private static final String TAG = "CustView";

    private int custColor = Color.GRAY;
    private Paint paint;

    public CustView(Context context) {
        this(context, null);
    }

    public CustView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustView);
        custColor = a.getColor(R.styleable.CustView_CustColor, Color.GRAY);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(custColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        int centX = getWidth() / 2;
        int centY = getHeight() / 2;
        float radius = getWidth() / 2f;
//        canvas.drawCircle(centX, centY, radius, paint);
        paint.setColor(Color.BLACK);
        int bian = 5;
        double offset = 2 * Math.PI / 12;
        for (int i = 0; i < 5; i++) {
            canvPath(canvas, paint, centX, centY, bian, offset, radius / 5 * (5 - i));
        }
        canvRe(canvas, paint, centX, centY, bian, offset, radius);
    }

    private void canvPath(Canvas canvas, Paint paint, float centX, float centY, int bian, double offset, float radius) {
        Path path = new Path();
        float startX = 0f, startY = 0f;
        for (int i = 0; i < bian; i++) {
            float x = centX - (float) Math.cos((2 * Math.PI / bian) * i + offset) * radius;
            float y = centY - (float) Math.sin((2 * Math.PI / bian) * i + offset) * radius;
            if (i == 0) {
                startX = x;
                startY = y;
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
//            Log.i(TAG, String.format(Locale.CHINA, "%d,-> x = %f,y = %f", i, x, y));
            paint.setColor(Color.BLACK);
            canvas.drawLine(centX, centY, x, y, paint);
        }
        path.lineTo(startX, startY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    private void canvRe(Canvas canvas, Paint paint, float centX, float centY, int bian, double offset, float radius) {
        Path path = new Path();
        paint.setColor(Color.RED);
        float startX = 0f, startY = 0f;
        for (int i = 0; i < bian; i++) {
            float bili = new Random().nextInt(10) / 10f;
            float tempR = radius * bili;
            Log.i(TAG, "canvRe: bili = " + bili);
            float x = centX - (float) Math.cos((2 * Math.PI / bian) * i + offset) * tempR;
            float y = centY - (float) Math.sin((2 * Math.PI / bian) * i + offset) * tempR;
            if (i == 0) {
                startX = x;
                startY = y;
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.lineTo(startX, startY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }
}
