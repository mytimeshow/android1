package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import cn.czyugang.tcg.client.R;


/**
 * Created by ruiaa on 2016/10/30.
 */

public class FlowLayout extends ViewGroup {

    private static final int DEFAULT_HORIZONTAL_SPACING = 5;
    private static final int DEFAULT_VERTICAL_SPACING = 5;
    private static final int DEFAULT_LINE_COUNT = 1000;

    protected int verticalSpacing;
    protected int horizontalSpacing;
    protected int lineCount;
    protected int colCount=-1;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        try {
            horizontalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_horizontal_spacing, DEFAULT_HORIZONTAL_SPACING);
            verticalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_vertical_spacing, DEFAULT_VERTICAL_SPACING);
            lineCount = a.getInteger(
                    R.styleable.FlowLayout_line_count, DEFAULT_LINE_COUNT);
            colCount = a.getInteger(R.styleable.FlowLayout_column_count, -1);
        } finally {
            a.recycle();
        }
    }


    public void setHorizontalSpacing(int pixelSize) {
        horizontalSpacing = pixelSize;
    }

    public void setVerticalSpacing(int pixelSize) {
        verticalSpacing = pixelSize;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int myWidth = resolveSize(0, widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        int childWidthMeasureSpec=widthMeasureSpec;
        if (colCount>0){
            int childMaxWidth=(myWidth-paddingLeft-paddingRight-horizontalSpacing*(colCount-1))/colCount;
            childWidthMeasureSpec=MeasureSpec.makeMeasureSpec(childMaxWidth,MeasureSpec.getMode(widthMeasureSpec));
        }


        int lines = 1;
        // Measure each child and put the child to the right of previous child
        // if there's enough room for it, otherwise, wrap the line and put the child to next line.
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View child = getChildAt(i);

            if (child.getVisibility() != View.GONE) {
                measureChild(child, childWidthMeasureSpec, heightMeasureSpec);
            } else {
                continue;
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {

                lines = lines + 1;
                if (lines > lineCount) {
                    child.setVisibility(GONE);
                    continue;
                }


                childLeft = paddingLeft;
                childTop += verticalSpacing + lineHeight;
                lineHeight = childHeight;
            } else {
                childLeft += childWidth + horizontalSpacing;
            }
        }

        int wantedHeight = childTop + lineHeight + paddingBottom;

        setMeasuredDimension(myWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int myWidth = r - l;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        int lines = 1;

        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);


            if (childView.getVisibility() == View.GONE) {
                continue;
            }


            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();


            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {

                lines = lines + 1;
                if (lines > lineCount) {
                    childView.setVisibility(GONE);
                    continue;
                }

                childLeft = paddingLeft;
                childTop += verticalSpacing + lineHeight;
                lineHeight = childHeight;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + horizontalSpacing;
        }
    }


}
