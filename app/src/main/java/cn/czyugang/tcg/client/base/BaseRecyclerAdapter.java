package cn.czyugang.tcg.client.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzihong on 2017/9/12.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    private final int VIEW_TYPE_HEADER = -1;
    private final int VIEW_TYPE_FOOTER = -2;

    protected Context context;
    protected RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;//每项点击事件监听器
    private OnItemCheckedChangeListener onItemCheckedChangeListener;//每项里面的点击事件监听器
    protected List<T> dataList;
    private List<View> mHeaderViewList;
    private List<View> mFooterViewList;
    //    private View mLoadMoreView;
//    private TextView tv_loading;
//    private ProgressBar progressBar;
//    private OnLoadMoreListener mOnLoadMoreListener;//加载更多监听器
//    private boolean isLoading = false;
    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            //设置GridLayoutManager的HeaderView和FooterView占满屏幕
            int viewType = getItemViewType(position);
            //设置头部跟底部的宽度
            return (viewType % 10 == VIEW_TYPE_HEADER || viewType % 10 == VIEW_TYPE_FOOTER) ? ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount() : 1;
        }
    };
//    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            if (mOnLoadMoreListener != null && mLoadMoreView != null && mLoadMoreView.isShown() && !isLoading) {
//                isLoading = true;
//                mOnLoadMoreListener.onLoadMore();
//            }
//        }
//    };

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<T> dataList) {
        this.dataList = dataList;
    }

    public void addHeaderView(View view) {
        if (mHeaderViewList == null) {
            mHeaderViewList = new ArrayList<>();
        }
        mHeaderViewList.add(view);
    }

    public void removeHeaderView(View view) {
        if (mHeaderViewList != null) {
            mHeaderViewList.remove(view);
        }
    }

    public void clearHeaderView() {
        if (mHeaderViewList != null) {
            mHeaderViewList.clear();
        }
    }

    public int getHeaderSize() {
        return mHeaderViewList == null ? 0 : mHeaderViewList.size();
    }

    public void addFooterView(View view) {
        if (mFooterViewList == null) {
            mFooterViewList = new ArrayList<>();
        }
        mFooterViewList.add(view);
    }

    public void removeFooterView(View view) {
        if (mFooterViewList != null) {
            mFooterViewList.remove(view);
        }
    }

    public void clearFooterView() {
        if (mFooterViewList != null) {
            mFooterViewList.clear();
        }
    }

    public int getFooterSize() {
        return mFooterViewList == null ? 0 : mFooterViewList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener listener) {
        onItemCheckedChangeListener = listener;
    }

//    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
//        mOnLoadMoreListener = listener;
//    }

//    /**
//     * 告诉设配器加载是否成功
//     *
//     * @param state
//     */
//    public void loadMoreFinish(boolean state, boolean loadMore) {
//        if (mLoadMoreView == null) {
//            mLoadMoreView = LayoutInflater.from(context).inflate(R.layout.load_more, recyclerView, false);
//            tv_loading = (TextView) mLoadMoreView.findViewById(R.id.tv_loading);
//            progressBar = (ProgressBar) mLoadMoreView.findViewById(R.id.progress_bar);
//            //点击重新加载
//            mLoadMoreView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnLoadMoreListener != null) {
//                        progressBar.setVisibility(View.VISIBLE);
//                        mLoadMoreView.setClickable(false);
//                        tv_loading.setText("加载中...");
//                        mOnLoadMoreListener.onLoadMore();
//                    }
//                }
//            });
//            addFooterView(mLoadMoreView);
//        }
//        if (state) {
//            mLoadMoreView.setClickable(false);
//            if (loadMore) {
//                progressBar.setVisibility(View.VISIBLE);
//                tv_loading.setText("加载中...");
//                isLoading = false;
//            } else {
//                tv_loading.setText("没有更多内容");
//                progressBar.setVisibility(View.GONE);
//                isLoading = true;
//            }
//        } else {
//            progressBar.setVisibility(View.GONE);
//            mLoadMoreView.setClickable(true);
//            tv_loading.setText("加载失败，点击重新加载");
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType % 10 == VIEW_TYPE_HEADER)
            return new RecyclerView.ViewHolder(mHeaderViewList.get(-viewType / 10)) {
            };
        if (viewType % 10 == VIEW_TYPE_FOOTER)
            return new RecyclerView.ViewHolder(mFooterViewList.get(-viewType / 10)) {
            };
        return onCreate(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType % 10 == VIEW_TYPE_HEADER) return;
        if (viewType % 10 == VIEW_TYPE_FOOTER) return;
        int p = mHeaderViewList == null ? position : position - mHeaderViewList.size();
        if (holder instanceof BaseRecyclerAdapter.ViewHolder) {
            ((ViewHolder) holder).onItemClickListenerHelper.setPosition(p);
            ((ViewHolder) holder).onItemCheckedChangeListenerHelper.setPosition(p);
            if (dataList != null && p < dataList.size()) {
                ((ViewHolder) holder).onItemClickListenerHelper.setData(dataList.get(p));
                ((ViewHolder) holder).onItemCheckedChangeListenerHelper.setData(dataList.get(p));
            }
        }
        onBind(holder, p);
    }

    @Override
    public int getItemCount() {
        int count = dataList == null ? 0 : dataList.size();
        if (mHeaderViewList != null) count = count + mHeaderViewList.size();
        if (mFooterViewList != null) count = count + mFooterViewList.size();
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderViewList != null && position < mHeaderViewList.size())
            return VIEW_TYPE_HEADER - position * 10;
        if (mFooterViewList != null && position >= getItemCount() - mFooterViewList.size())
            return VIEW_TYPE_FOOTER - (position - getItemCount() + mFooterViewList.size()) * 10;
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
//        this.recyclerView.addOnScrollListener(onScrollListener);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            //设置GridLayoutManager每列所占的列数
            ((GridLayoutManager) manager).setSpanSizeLookup(mSpanSizeLookup);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        //设置StaggeredGridLayoutManager的HeaderView和FooterView为1列
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            int viewType = getItemViewType(holder.getLayoutPosition());
            //设置头部跟底部的宽度
            if (viewType % 10 == VIEW_TYPE_HEADER || viewType % 10 == VIEW_TYPE_FOOTER)
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
        }
    }

    protected abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType);

    protected abstract void onBind(RecyclerView.ViewHolder holder, int position);

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public OnItemClickListenerHelper onItemClickListenerHelper;
        public OnItemCheckedChangeListenerHelper onItemCheckedChangeListenerHelper;

        public ViewHolder(View itemView) {
            super(itemView);
            onItemClickListenerHelper = new OnItemClickListenerHelper(onItemClickListener, recyclerView);
            itemView.setOnClickListener(onItemClickListenerHelper);
            onItemCheckedChangeListenerHelper = new OnItemCheckedChangeListenerHelper(onItemCheckedChangeListener, recyclerView);
        }
    }

    /**
     * RecyclerAdapter内部点击监听器
     */
    public class OnItemClickListenerHelper implements View.OnClickListener {
        private BaseRecyclerAdapter.OnItemClickListener mListener;
        private RecyclerView mRecyclerView;
        private int mPosition;
        private Object mData;

        private OnItemClickListenerHelper(BaseRecyclerAdapter.OnItemClickListener listener, RecyclerView recyclerView) {
            mListener = listener;
            mRecyclerView = recyclerView;
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
                mListener.onItemClick(mRecyclerView, v, mPosition, mData);
            }
        }
    }

    /**
     * RecyclerAdapter内部选中改变监听器
     */
    public class OnItemCheckedChangeListenerHelper implements CompoundButton.OnCheckedChangeListener {
        private OnItemCheckedChangeListener mListener;
        private RecyclerView mRecyclerView;
        private int mPosition;
        private Object mData;

        private OnItemCheckedChangeListenerHelper(OnItemCheckedChangeListener listener, RecyclerView recyclerView) {
            mListener = listener;
            mRecyclerView = recyclerView;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public void setData(Object data) {
            mData = data;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mListener != null) {
                mListener.onItemCheckedChanged(mRecyclerView, buttonView, mPosition, mData, isChecked);
            }
        }
    }

//    public interface OnLoadMoreListener {
//        void onLoadMore();
//    }

    /**
     * RecyclerAdapter点击监听器
     */
    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position, Object data);
    }

    /**
     * RecyclerAdapter选中改变监听器
     */
    public interface OnItemCheckedChangeListener {
        void onItemCheckedChanged(RecyclerView recyclerView, CompoundButton view, int position, Object data, boolean isChecked);
    }
}
