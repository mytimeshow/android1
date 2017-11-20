package cn.czyugang.tcg.client.base;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/6/28.
 * 默认的列表分割线
 */
public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HEADER_DECORATION = 0;//头部有分割线
    public static final int FOOTER_DECORATION = 1;//底部有分割线
    public static final int HEADER_FOOTER_DECORATION = 2;//头尾都有分割线
    public static final int NOT_HEADER_FOOTER_DECORATION = 3;//头尾都没有分割线
    private Resources mResources;
    private Paint mPaint;
    private float mDividerWidth;//分割线高度
    private int mDecorationPosition;//分割线位置标识

    /**
     * 默认分割线
     *
     * @param resources
     * @param decorationPosition
     */
    public DefaultItemDecoration(Resources resources, int decorationPosition) {
        mResources = resources;
        mDecorationPosition = decorationPosition;
    }

    public void setDividerHeightResource(@DimenRes int height) {
        mDividerWidth = mResources.getDimension(height);
    }

    public void setDividerHeight(float height) {
        mDividerWidth = height;
    }

    public void setDividerColorResource(@ColorRes int color) {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setColor(mResources.getColor(color));
    }

    public void setDividerColor(@ColorInt int color) {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //画笔为空，无需画线
        if (mPaint == null)
            return;
        if (layoutManager instanceof GridLayoutManager) {
            drawGrid(((GridLayoutManager) layoutManager).getOrientation(), c, parent, ((GridLayoutManager) layoutManager).getSpanCount());
        } else if (layoutManager instanceof LinearLayoutManager) {
            drawLinear(((LinearLayoutManager) layoutManager).getOrientation(), c, parent);
        }
    }

    public void drawLinear(int orientation, Canvas c, RecyclerView parent) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int headerCount = 0;
        int footerCount = 0;
        if (adapter instanceof BaseRecyclerAdapter) {
            headerCount = ((BaseRecyclerAdapter) adapter).getHeaderSize();
            footerCount = ((BaseRecyclerAdapter) adapter).getFooterSize();
        }
        int count = adapter.getItemCount() - headerCount - footerCount;
        int childCount = parent.getChildCount();
        switch (mDecorationPosition) {
            case HEADER_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    position = position - headerCount;
                    if (position == 0) {
                        //第一个View上方加上分割线
                        if (orientation == LinearLayoutManager.VERTICAL) {
                            c.drawRect(child.getLeft(), child.getTop() - mDividerWidth, child.getRight(), child.getTop(), mPaint);
                        } else {
                            c.drawRect(child.getLeft() - mDividerWidth, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                        }
                    }
                    if (position + 1 < count) {
                        //所有View（除了最后一个）下方加上分割线
                        if (orientation == LinearLayoutManager.VERTICAL) {
                            c.drawRect(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom() + mDividerWidth, mPaint);
                        } else {
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                        }
                    }
                }
                break;
            case FOOTER_DECORATION:
                //所有View下方加上分割线
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        c.drawRect(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom() + mDividerWidth, mPaint);
                    } else {
                        c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                    }
                }
                break;
            case HEADER_FOOTER_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    position = position - headerCount;
                    if (position == 0) {
                        //第一个View上方加上分割线
                        if (orientation == LinearLayoutManager.VERTICAL) {
                            c.drawRect(child.getLeft(), child.getTop() - mDividerWidth, child.getRight(), child.getTop(), mPaint);
                        } else {
                            c.drawRect(child.getLeft() - mDividerWidth, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                        }
                    }
                    //所有View下方加上分割线
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        c.drawRect(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom() + mDividerWidth, mPaint);
                    } else {
                        c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                    }
                }
                break;
            case NOT_HEADER_FOOTER_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    position = position - headerCount;
                    if (position + 1 < count) {
                        //所有View（除了最后一个）下方加上分割线
                        if (orientation == LinearLayoutManager.VERTICAL) {
                            c.drawRect(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom() + mDividerWidth, mPaint);
                        } else {
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                        }
                    }
                }
                break;
        }
    }

    private void drawGrid(int orientation, Canvas c, RecyclerView parent, int span) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int headerCount = 0;
        int footerCount = 0;
        if (adapter instanceof BaseRecyclerAdapter) {
            headerCount = ((BaseRecyclerAdapter) adapter).getHeaderSize();
            footerCount = ((BaseRecyclerAdapter) adapter).getFooterSize();
        }
        int count = adapter.getItemCount() - headerCount - footerCount;
        int childCount = parent.getChildCount();
        switch (mDecorationPosition) {
            case HEADER_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    position = position - headerCount;
                    if (position < span) {
                        //第一行View上方加上分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop() - mDividerWidth, child.getRight() + mDividerWidth / 2, child.getTop(), mPaint);
                    }
                    if (position < count - count % span) {
                        //所有View（除了最后一行）下方加上分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getBottom(), child.getRight() + mDividerWidth / 2, child.getBottom() + mDividerWidth, mPaint);
                    }
                    if ((position + 1) % span == 1) {
                        //第一列右方加上一半分割线
                        c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                    } else if ((position + 1) % span == 0) {
                        //最后一列左方加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                    } else {
                        //左右两边加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                        if (position + 1 == count) {
                            //如果是最后一项，右方加上完整分割线
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                        } else {
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                        }
                    }
                }
                break;
            case FOOTER_DECORATION:
                //所有View下方加上分割线
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    position = position - headerCount;
                    //所有View下方加上分割线
                    if (count % span != 0 && position + 1 == count) {
                        //如果是最后一项
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getBottom(), child.getRight() + mDividerWidth, child.getBottom() + mDividerWidth, mPaint);
                    } else {
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getBottom(), child.getRight() + mDividerWidth / 2, child.getBottom() + mDividerWidth, mPaint);
                    }
                    if ((position + 1) % span == 1) {
                        //第一列右方加上一半分割线
                        c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                    } else if ((position + 1) % span == 0) {
                        //最后一列左方加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                    } else {
                        //左右两边加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                        if (position + 1 == count) {
                            //如果是最后一项，右方加上完整分割线
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                        } else {
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                        }
                    }
                }
                break;
            case HEADER_FOOTER_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    position = position - headerCount;
                    if (position < span) {
                        //第一行View上方加上分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop() - mDividerWidth, child.getRight() + mDividerWidth / 2, child.getTop(), mPaint);
                    }
                    //所有View下方加上分割线
                    if (count % span != 0 && position + 1 == count) {
                        //如果是最后一项
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getBottom(), child.getRight() + mDividerWidth, child.getBottom() + mDividerWidth, mPaint);
                    } else {
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getBottom(), child.getRight() + mDividerWidth / 2, child.getBottom() + mDividerWidth, mPaint);
                    }
                    if ((position + 1) % span == 1) {
                        //第一列右方加上一半分割线
                        c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                    } else if ((position + 1) % span == 0) {
                        //最后一列左方加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                    } else {
                        //左右两边加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                        if (position + 1 == count) {
                            //如果是最后一项，右方加上完整分割线
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                        } else {
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                        }
                    }
                }
                break;
            case NOT_HEADER_FOOTER_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (footerCount > 0 && position >= count + headerCount)
                        continue;
                    if (position < headerCount)
                        continue;
                    position = position - headerCount;
                    if (position < count - count % span) {
                        //所有View（除了最后一行）下方加上分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getBottom(), child.getRight() + mDividerWidth / 2, child.getBottom() + mDividerWidth / 2, mPaint);
                    }
                    if ((position + 1) % span == 1) {
                        //第一列右方加上一半分割线
                        c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                    } else if ((position + 1) % span == 0) {
                        //最后一列左方加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                    } else {
                        //左右两边加上一半分割线
                        c.drawRect(child.getLeft() - mDividerWidth / 2, child.getTop(), child.getLeft(), child.getBottom(), mPaint);
                        if (position + 1 == count) {
                            //如果是最后一项，右方加上完整分割线
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth, child.getBottom(), mPaint);
                        } else {
                            c.drawRect(child.getRight(), child.getTop(), child.getRight() + mDividerWidth / 2, child.getBottom(), mPaint);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            getGridItemOffsets(((GridLayoutManager) layoutManager).getOrientation(), outRect, view, parent, ((GridLayoutManager) layoutManager).getSpanCount());
        } else if (layoutManager instanceof LinearLayoutManager) {
            getLinearItemOffsets(((LinearLayoutManager) layoutManager).getOrientation(), outRect, view, parent);
        }
    }

    public void getLinearItemOffsets(int orientation, Rect outRect, View view, RecyclerView parent) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int headerCount = 0;
        int footerCount = 0;
        if (adapter instanceof BaseRecyclerAdapter) {
            headerCount = ((BaseRecyclerAdapter) adapter).getHeaderSize();
            footerCount = ((BaseRecyclerAdapter) adapter).getFooterSize();
        }
        int count = adapter.getItemCount() - headerCount - footerCount;
        int position = parent.getChildAdapterPosition(view);
        if (footerCount > 0 && position >= count + headerCount)
            return;
        if (position < headerCount)
            return;
        position = position - headerCount;
        switch (mDecorationPosition) {
            case HEADER_DECORATION:
                if (position == 0) {
                    //第一个View上方加上偏移
                    if (position + 1 < count) {
                        if (orientation == LinearLayoutManager.VERTICAL) {
                            outRect.set(0, (int) mDividerWidth, 0, (int) mDividerWidth);
                        } else {
                            outRect.set((int) mDividerWidth, 0, (int) mDividerWidth, 0);
                        }
                    } else {
                        if (orientation == LinearLayoutManager.VERTICAL) {
                            outRect.set(0, (int) mDividerWidth, 0, 0);
                        } else {
                            outRect.set((int) mDividerWidth, 0, 0, 0);
                        }
                    }
                } else if (position + 1 < count) {
                    //所有View（除了最后一个）下方加上偏移
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        outRect.set(0, 0, 0, (int) mDividerWidth);
                    } else {
                        outRect.set(0, 0, (int) mDividerWidth, 0);
                    }
                }
                break;
            case FOOTER_DECORATION:
                //所有View下方加上偏移
                if (orientation == LinearLayoutManager.VERTICAL) {
                    outRect.set(0, 0, 0, (int) mDividerWidth);
                } else {
                    outRect.set(0, 0, (int) mDividerWidth, 0);
                }
                break;
            case HEADER_FOOTER_DECORATION:
                if (position == 0) {
                    //第一个View上方加上偏移
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        outRect.set(0, (int) mDividerWidth, 0, (int) mDividerWidth);
                    } else {
                        outRect.set((int) mDividerWidth, 0, (int) mDividerWidth, 0);
                    }
                } else {
                    //所有View下方加上偏移
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        outRect.set(0, 0, 0, (int) mDividerWidth);
                    } else {
                        outRect.set(0, 0, (int) mDividerWidth, 0);
                    }
                }
                break;
            case NOT_HEADER_FOOTER_DECORATION:
                if (position + 1 < count) {
                    //所有View（除了最后一个）下方加上偏移
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        outRect.set(0, 0, 0, (int) mDividerWidth);
                    } else {
                        outRect.set(0, 0, (int) mDividerWidth, 0);
                    }
                }
                break;
        }
    }

    public void getGridItemOffsets(int orientation, Rect outRect, View view, RecyclerView parent, int span) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int headerCount = 0;
        int footerCount = 0;
        if (adapter instanceof BaseRecyclerAdapter) {
            headerCount = ((BaseRecyclerAdapter) adapter).getHeaderSize();
            footerCount = ((BaseRecyclerAdapter) adapter).getFooterSize();
        }
        int count = adapter.getItemCount() - headerCount - footerCount;
        int position = parent.getChildAdapterPosition(view);
        if (footerCount > 0 && position >= count + headerCount)
            return;
        if (position < headerCount)
            return;
        position = position - headerCount;
        switch (mDecorationPosition) {
            case HEADER_DECORATION:
                if (position < span) {
                    //第一行View上方加上偏移
                    if (position < count - count % span) {
                        outRect.set((int) (mDividerWidth / 2), (int) mDividerWidth, (int) (mDividerWidth / 2), (int) mDividerWidth);
                    } else {
                        outRect.set((int) (mDividerWidth / 2), (int) mDividerWidth, (int) (mDividerWidth / 2), 0);
                    }
                } else if (position < count - count % span) {
                    //所有View下方加上偏移
                    outRect.set((int) (mDividerWidth / 2), 0, (int) (mDividerWidth / 2), (int) mDividerWidth);
                } else {
                    //左右两边加上一半分割线
                    outRect.set((int) (mDividerWidth / 2), 0, (int) (mDividerWidth / 2), 0);
                }
                break;
            case FOOTER_DECORATION:
                //所有View下方加上偏移
                outRect.set((int) (mDividerWidth / 2), 0, (int) (mDividerWidth / 2), (int) mDividerWidth);
                break;
            case HEADER_FOOTER_DECORATION:
                if (position < span) {
                    //第一行View上方加上偏移
                    if (position < count - count % span) {
                        outRect.set((int) (mDividerWidth / 2), (int) mDividerWidth, (int) (mDividerWidth / 2), (int) mDividerWidth);
                    } else {
                        outRect.set((int) (mDividerWidth / 2), (int) mDividerWidth, (int) (mDividerWidth / 2), 0);
                    }
                } else {
                    //所有View下方加上偏移
                    outRect.set((int) (mDividerWidth / 2), 0, (int) (mDividerWidth / 2), (int) mDividerWidth);
                }
                break;
            case NOT_HEADER_FOOTER_DECORATION:
                if (position < count - count % span) {
                    //所有View下方加上偏移
                    outRect.set((int) (mDividerWidth / 2), 0, (int) (mDividerWidth / 2), (int) mDividerWidth);
                } else {
                    //左右两边加上一半分割线
                    outRect.set((int) (mDividerWidth / 2), 0, (int) (mDividerWidth / 2), 0);
                }
                break;
        }
    }
}
