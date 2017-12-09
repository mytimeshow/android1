package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * Created by ruiaa on 2017/8/7.
 */

public class RecyclerViewMaxH extends RecyclerView {

    private int maxHeight=-1;

    public RecyclerViewMaxH(Context context) {
        super(context);
    }

    public RecyclerViewMaxH(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewMaxH(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight!=-1) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
        super.onMeasure(widthSpec, heightSpec);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
