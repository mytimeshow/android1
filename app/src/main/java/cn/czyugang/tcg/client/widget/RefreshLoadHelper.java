package cn.czyugang.tcg.client.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

import cn.czyugang.tcg.client.R;

/**
 * @author ruiaa
 * @date 2017/12/8
 */

public class RefreshLoadHelper {

    public SwipeToLoadLayout swipeToLoadLayout=null;
    private RefreshHeaderView refreshHeaderView=null;
    private LoadMoreFooterView loadMoreFooterView=null;

    public RefreshLoadHelper(Activity activity){
        swipeToLoadLayout=new SwipeToLoadLayout(activity);
        refreshHeaderView=new RefreshLoadHelper.RefreshHeaderView(activity);
        loadMoreFooterView=new RefreshLoadHelper.LoadMoreFooterView(activity);
        swipeToLoadLayout.setOnRefreshListener(()->{
            swipeToLoadLayout.postDelayed(()->{
                swipeToLoadLayout.setRefreshing(false);
            },2000);
        });
        swipeToLoadLayout.setOnLoadMoreListener(()->{
            swipeToLoadLayout.postDelayed(()->{
                swipeToLoadLayout.setLoadingMore(false);
            },2000);
        });
    }

    public RefreshLoadHelper build(RecyclerView recyclerView){
        swipeToLoadLayout.setLayoutParams(recyclerView.getLayoutParams());
        ViewGroup viewGroup=(ViewGroup)(recyclerView.getParent());
        int viewIndex=viewGroup.indexOfChild(recyclerView);
        viewGroup.removeView(recyclerView);
        recyclerView.setLayoutParams(new ViewGroup.MarginLayoutParams(-1,-1));
        refreshHeaderView.setLayoutParams(new ViewGroup.MarginLayoutParams(-1,100));
        loadMoreFooterView.setLayoutParams(new ViewGroup.MarginLayoutParams(-1,100));
        swipeToLoadLayout.addView(refreshHeaderView);
        swipeToLoadLayout.addView(recyclerView);
        swipeToLoadLayout.addView(loadMoreFooterView);
        swipeToLoadLayout.refreshViewIdentity();
        viewGroup.addView(swipeToLoadLayout,viewIndex);
        return this;
    }



    /**
     * Created by ruiaa on 2017/11/13.
     */

    public static class RefreshHeaderView extends TextView implements SwipeRefreshTrigger, SwipeTrigger {

        public RefreshHeaderView(Context context) {
            super(context);
            setGravity(Gravity.CENTER_VERTICAL);

        }

        public RefreshHeaderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setGravity(Gravity.CENTER_VERTICAL);
        }

        @Override
        public void onRefresh() {
            setText("正在刷新  onRefresh");
        }

        @Override
        public void onPrepare() {
            setText("");
        }

        @Override
        public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
            if (!isComplete) {
                if (yScrolled >= getHeight()) {
                    setText("释放刷新");
                } else {
                    setText("下拉");
                }
            } else {

            }
        }

        @Override
        public void onRelease() {

        }

        @Override
        public void onComplete() {
            setText("刷新完成");
        }

        @Override
        public void onReset() {
            setText("");
        }
    }

    /**
     * Created by ruiaa on 2017/11/13.
     */

    public static class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
        public LoadMoreFooterView(Context context) {
            super(context);
            setGravity(Gravity.CENTER_VERTICAL);

        }

        public LoadMoreFooterView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setGravity(Gravity.CENTER_VERTICAL);
        }

        @Override
        public void onLoadMore() {
            setText("正在加载  onLoadMore");
        }

        @Override
        public void onPrepare() {
            setText("");
        }

        @Override
        public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
            if (!isComplete) {
                if (yScrolled <= -getHeight()) {
                    setText("释放加载");
                } else {
                    setText("上拉");
                }
            } else {
                setText("正在加载 returning");
            }
        }

        @Override
        public void onRelease() {
            setText("正在加载  onRelease");
        }

        @Override
        public void onComplete() {
            setText("加载完成  onComplete");
        }

        @Override
        public void onReset() {
            setText("");
        }
    }
}
