package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * @author ruiaa
 * @date 2017/12/5
 */

public class ProgressBgView extends TextView {
    public ProgressBgView(Context context) {
        super(context);
    }

    public ProgressBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float progress = 0.05f;
    private Paint paint = null;
    private int leftColor = Color.BLUE;
    private int rightColor = Color.YELLOW;
    private Xfermode xfermode = null;

    @Override
    protected void onDraw(Canvas canvas) {
        if (paint == null) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        }
        int w = getWidth();
        int h = getHeight();

        //setLayerType(LAYER_TYPE_HARDWARE, null);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        canvas.drawColor(Color.TRANSPARENT);
        paint.setColor(Color.RED);
        canvas.drawCircle(h / 2, h / 2, h / 2, paint);
        canvas.drawCircle(w - h / 2, h / 2, h / 2, paint);
        canvas.drawRect(h / 2, 0, w - h / 2, h, paint);

        paint.setXfermode(xfermode);
        paint.setColor(leftColor);
        canvas.drawRect(0, 0, w * progress, h, paint);
        paint.setColor(rightColor);
        canvas.drawRect(w - w * progress, 0, w, h, paint);
        paint.setXfermode(null);

    }

    public  ProgressBgView  setProgress(float progress) {
        if (progress < 0 || progress > 1) return this;
        this.progress = progress;
        setText(String.valueOf((int) (progress * 100)) + "%");
        return this;
    }

    public ProgressBgView setLeftColor(int leftColor) {
        this.leftColor = leftColor;
        invalidate();
        return this;
    }

    public ProgressBgView setRightColor(int rightColor) {
        this.rightColor = rightColor;
        invalidate();
        return this;
    }

    public ProgressBgView setLeftColorRes(@ColorRes int leftColor) {
        this.leftColor = ResUtil.getColor(leftColor);
        return this;
    }

    public ProgressBgView setRightColorRes(@ColorRes int rightColor) {
        this.rightColor = ResUtil.getColor(rightColor);
        return this;
    }
}
