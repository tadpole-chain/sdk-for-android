package com.tadpolechain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2015/10/12.
 */
public class GlideCircleTransform extends BitmapTransformation {

    private static float conner = 0.5F;

    private static boolean square = true;

    public GlideCircleTransform(Context context) {
        super(context);
    }

    public GlideCircleTransform(Context context, float conner) {
        super(context);
        GlideCircleTransform.conner = conner;
    }

    public GlideCircleTransform(Context context, boolean square, float conner) {
        super(context);
        GlideCircleTransform.conner = conner;
        GlideCircleTransform.square = square;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        if(square) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            canvas.drawRoundRect(new RectF(0, 0, size, size), conner * size, conner * size, paint);
            return result;
        } else {
            int size = Math.min(source.getWidth(), source.getHeight());

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP,
                    BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            canvas.drawRoundRect(new RectF(0, 0, source.getWidth(), source.getHeight()),
                    conner * size, conner * size, paint);
            return result;
        }
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
