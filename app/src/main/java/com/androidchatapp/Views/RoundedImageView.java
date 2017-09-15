package com.androidchatapp.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class RoundedImageView extends AppCompatImageView {

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp) {

        Bitmap output = null;

        try {
            output = Bitmap.createBitmap(bmp.getWidth(),
                    bmp.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.parseColor("#BAB399"));
            canvas.drawCircle(bmp.getWidth() / 2, bmp.getHeight() / 2,//
                    bmp.getWidth() / 2, paint); //
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bmp, rect, rect, paint);
        } catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
        }

        return output;
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();

        Bitmap roundBitmap = getCroppedBitmap(b);

        super.setImageDrawable(new BitmapDrawable(getResources(), roundBitmap));
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(getCroppedBitmap(bm));
    }
}