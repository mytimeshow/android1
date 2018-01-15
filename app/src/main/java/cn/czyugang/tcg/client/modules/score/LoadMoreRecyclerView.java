package cn.czyugang.tcg.client.modules.score;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class LoadMoreRecyclerView  extends RecyclerView{
    int contentHeight=this.computeVerticalScrollExtent();
    int scrolledHeight=this.computeVerticalScrollOffset();
    int recyclerviewHeight=this.computeVerticalScrollRange();



    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {


        if (contentHeight + scrolledHeight >= recyclerviewHeight
                && contentHeight>recyclerviewHeight){

        } ;


        return super.onTouchEvent(e);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


    }
}
