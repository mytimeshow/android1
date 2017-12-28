package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author ruiaa
 * @date 2017/12/29
 */

public class ViewPagerWrap extends ViewPager {
    public ViewPagerWrap(Context context) {
        super(context);
    }

    public ViewPagerWrap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean measureW=false;
        boolean measureH=false;
        if (MeasureSpec.getMode(widthMeasureSpec)==MeasureSpec.UNSPECIFIED){
            measureW=true;
            widthMeasureSpec=MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        if (MeasureSpec.getMode(heightMeasureSpec)==MeasureSpec.UNSPECIFIED){
            measureH=true;
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        if (measureW||measureH) {
            int width=0;
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, heightMeasureSpec);
                int w = child.getMeasuredWidth();
                if (w > width) width = w;
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }

            if (measureW) widthMeasureSpec=MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            if (measureH) heightMeasureSpec=MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
