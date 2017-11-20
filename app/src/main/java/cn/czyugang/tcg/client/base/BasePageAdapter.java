package cn.czyugang.tcg.client.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzihong on 2017/9/12.
 * PagerAdapter基类
 */

public abstract class BasePageAdapter extends PagerAdapter {
    protected Context context;
    protected List dataList;
    private List<ViewHolder> mConvertViewHolder;
    private ViewPager mViewPager;

    private OnItemClickListener onItemClickListener;//每项点击事件监听器

    public BasePageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ViewHolder) object).itemView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mViewPager == null && container instanceof ViewPager) {
            mViewPager = (ViewPager) container;
        }
        ViewHolder holder;
        if (mConvertViewHolder != null && mConvertViewHolder.size() > 0) {
            holder = mConvertViewHolder.get(0);
            mConvertViewHolder.remove(0);
        } else {
            holder = onCreate(container);
        }
        if (holder.onClickListenerHelper != null) {
            holder.onClickListenerHelper.setPosition(position);
            if (dataList != null) {
                holder.onClickListenerHelper.setData(dataList.get(position));
            }
        }
        onBind(holder, position);
        container.addView(holder.itemView);
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mConvertViewHolder == null) {
            mConvertViewHolder = new ArrayList<>();
        }
        mConvertViewHolder.add((ViewHolder) object);
        container.removeView(((ViewHolder) object).itemView);
    }

    public void setData(List dataList) {
        this.dataList = dataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    protected abstract ViewHolder onCreate(ViewGroup parent);

    protected abstract void onBind(ViewHolder holder, int position);

    protected class ViewHolder {
        public OnClickListenerHelper onClickListenerHelper = new OnClickListenerHelper(onItemClickListener, mViewPager);
        public View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            itemView.setOnClickListener(onClickListenerHelper);
        }
    }

    /**
     * PagerAdapter内部点击监听器
     */
    protected class OnClickListenerHelper implements View.OnClickListener {
        private BasePageAdapter.OnItemClickListener mListener;
        private ViewPager mViewPager;
        private int mPosition;
        private Object mData;

        private OnClickListenerHelper(BasePageAdapter.OnItemClickListener listener, ViewPager viewPager) {
            mListener = listener;
            mViewPager = viewPager;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public void setData(Object data) {
            mData = data;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(mViewPager, v, mPosition, mData);
            }
        }
    }

    /**
     * PagerAdapter每一项点击监听器
     */
    public interface OnItemClickListener {
        void onItemClick(ViewPager viewPager, View view, int position, Object data);
    }
}
