package com.tadpolechain;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lsq on 2018/4/11.
 */
public class MarqueeTextView extends View {

    private String text = "   ";

    private String tct = "";

    Handler handler = new Handler();

    float size = 40;

    Paint paint;

    long times = 0;

    int sw = 0;
    int sh = 0;
    float tw = 0;

    OnShowFinish onShowFinish;

    public MarqueeTextView(Context context) {
        this(context, null);
        init();
    }
    public MarqueeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setText(String text) {
        this.text = "" + text;
        if(this.text.contains("&")){
            String[] str = this.text.split("&");
            if(str.length == 2){
                this.text = str[0];
                tct = str[1];
            } else {
                tct = "";
            }
        } else {
            tct = "";
        }
        times = 0;
        if(this.getVisibility() == View.GONE){
            this.setVisibility(View.VISIBLE);
        }
        handler.postDelayed(update, 20);
        postInvalidate();
    }

    public String getText() {
        if(tct == null || tct.length() == 0){
            return text;
        } else {
            return text + "&" + tct;
        }
    }

    public void setOnShowFinish(OnShowFinish onShowFinish) {
        this.onShowFinish = onShowFinish;
    }

    public void init(){
        paint = new Paint();
        paint.setTextSize(size);
        paint.setTextAlign(Paint.Align.LEFT);
        handler.postDelayed(update, 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        sw = getWidth();
        sh = (int)(getHeight() + size) / 2;

        if(tct == null || tct.length() == 0){
            tw = paint.measureText(text);
            paint.setColor(0xffffffff);
            canvas.drawText(text, sw - times, sh, paint);
        } else {
            tw = paint.measureText(text + tct);
            paint.setColor(0xffffffff);
            canvas.drawText(text, sw - times, sh, paint);
            paint.setColor(0xff58c0f1);
            canvas.drawText(tct, sw - times + paint.measureText(text), sh, paint);
        }
    }

    Runnable update = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(update);
            if(MarqueeTextView.this.getVisibility() == View.GONE){
                return;
            }
            times += sw / 400;
            if(times > sw + tw) {
                times = 0;
                if(onShowFinish != null){
                    onShowFinish.finish();
                }
            }
            postInvalidate();
            handler.postDelayed(update, 20);
        }
    };

    public interface OnShowFinish{
        public void finish();
    }
}
