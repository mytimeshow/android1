package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.utils.app.ResUtil;


/**
 * Created by ruiaa on 2017/8/7.
 */

public class RecyclerViewMaxH extends RecyclerView {

    private int maxHeight = -1;

    public RecyclerViewMaxH(Context context) {
        super(context);
    }

    public RecyclerViewMaxH(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parse(context, attrs);
    }

    public RecyclerViewMaxH(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parse(context, attrs);
    }

    private void parse(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewMaxH);
        try {
            maxHeight = a.getDimensionPixelSize(R.styleable.RecyclerViewMaxH_max_height, maxHeight);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight != -1) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
        super.onMeasure(widthSpec, heightSpec);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMaxHeightRes(@DimenRes int maxHeight) {
        this.maxHeight = ResUtil.getDimenInPx(maxHeight);
    }
}
