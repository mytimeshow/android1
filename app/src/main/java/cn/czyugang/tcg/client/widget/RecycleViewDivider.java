package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruiaa on 2017/9/26.
 */

public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = 2;//分割线高度，默认为1px
    private int mDividerWidth = 2;//分割线宽度，默认为1px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private int offsetType = 0;//0 不偏移；1 右或下；2 左或上；3 左右或上下；4 所有
    private int fromTop = -1;
    private int fromLeft = -1;

    private boolean drawBottom = false;
    private boolean drawTop = false;
    private boolean drawRight = false;
    private boolean drawLeft = false;

    public RecycleViewDivider(Context context) {
        this(context, LinearLayoutManager.HORIZONTAL);
    }

    //默认分割线：高度为2px，颜色为灰色
    public RecycleViewDivider(Context context, int orientation) {
        mOrientation = orientation;
        mDivider = new ColorDrawable(0xffeeeeee);
    }

    //自定义分割线
    public RecycleViewDivider(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        int h = mDivider.getIntrinsicHeight();
        mDividerHeight = h <= 0 ? mDividerWidth : h;
        int w = mDivider.getIntrinsicWidth();
        mDividerWidth = w <= 0 ? mDividerWidth : w;
    }

    //自定义分割线
    public RecycleViewDivider(Context context, int orientation, int dividerSize, int dividerColor) {
        this(context, orientation);
        mDividerHeight = mDividerWidth = dividerSize;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, mDividerWidth, 0);
        } else {
            outRect.set(0, 0, mDividerWidth, mDividerHeight);
        }
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        }
    }

    private boolean hadAddTopPadding = false;
    private boolean hadAddLeftPadding = false;

    //绘制横向 item 分割线
    protected void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        List<Integer> list = new ArrayList<>();
        for (int i = 0,childSize = parent.getChildCount(); i < childSize; i++) {
            if (!drawBottom && i == childSize - 1) return;
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerHeight;
            if (list.contains(top)) continue;
            list.add(top);
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

            if (drawTop && i == 0) {
                if (!hadAddTopPadding) {
                    hadAddTopPadding = true;
                    child.setPadding(child.getPaddingLeft(), child.getPaddingTop() + mDividerHeight,
                            child.getPaddingRight(), child.getPaddingBottom());
                }
                top = child.getTop() + layoutParams.topMargin;
                bottom = top + mDividerHeight;
                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
                if (mPaint != null) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }

    //绘制纵向 item 分割线
    protected void drawVertical(Canvas canvas, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        int childSize = parent.getChildCount();

        if (fromTop != -1) {
            for (int i = 0; i < childSize; i++) {
                if (i == fromTop) {
                    final View child = parent.getChildAt(i);
                    top = child.getTop();
                }
            }
        }

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + layoutParams.rightMargin;
            int right = left + mDividerWidth;
            if (list.contains(left)) continue;
            list.add(left);
            if (!drawRight) {
                if (left + child.getWidth() > parent.getWidth()) continue;
            }

            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

            if (i == 0 && drawLeft) {
                if (!hadAddLeftPadding) {
                    hadAddLeftPadding = true;
                    child.setPadding(child.getPaddingLeft() + mDividerWidth, child.getPaddingTop(),
                            child.getPaddingRight(), child.getPaddingBottom());
                }
                left = child.getLeft();
                right = left + mDividerWidth;
                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
                if (mPaint != null) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }

    public RecycleViewDivider setOffsetType(int offsetType) {
        this.offsetType = offsetType;
        return this;
    }

    public RecycleViewDivider setFromTop(int fromTop) {
        this.fromTop = fromTop;
        return this;
    }

    public RecycleViewDivider setFromLeft(int fromLeft) {
        this.fromLeft = fromLeft;
        return this;
    }

    public RecycleViewDivider setDrawBottom(boolean drawBottom) {
        this.drawBottom = drawBottom;
        return this;
    }

    public RecycleViewDivider setDrawTop(boolean drawTop) {
        this.drawTop = drawTop;
        return this;
    }
}
