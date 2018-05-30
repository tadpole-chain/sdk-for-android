package com.tadpolechain;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by lsq on 2018/4/11.
 */
public class FloatButton extends ImageView {

    private int screenWidth, screenHeight;

    int lastX, lastY; // 记录移动的最后的位置

    long start_time = 0;

    boolean moving = false;

    public FloatButton(Context context) {
        this(context, null);
    }
    public FloatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ea = event.getAction();
        switch (ea) {
            case MotionEvent.ACTION_DOWN: // 按下
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                start_time = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE: // 移动
                // 移动中动态设置位置
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                // 将当前的位置再次设置
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                if(System.currentTimeMillis() - start_time > 400) {
                    moving = true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
