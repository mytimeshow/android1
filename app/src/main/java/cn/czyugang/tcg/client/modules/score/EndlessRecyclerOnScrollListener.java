package cn.czyugang.tcg.client.modules.score;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/1/13 0013.
 */



public  class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessRecyclerOnScroll";
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int screenHeight;
    private boolean isFinished;
    private int currentPage = 1;
    public OnLoadingListenner listenner;
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(
            LinearLayoutManager linearLayoutManager,int screenHeight) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.screenHeight=screenHeight;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int contentHeight=recyclerView.computeVerticalScrollExtent();
        int scrolledHeight=recyclerView.computeVerticalScrollOffset();
        int recyclerviewHeight=recyclerView.computeVerticalScrollRange();

       // hidLoading(recyclerView);

        if (contentHeight + scrolledHeight >= recyclerviewHeight && recyclerviewHeight>screenHeight) {

            onLoadMore(currentPage);
            //showLoading(recyclerView);

        }
        if(recyclerView.getChildCount()>0) {
            View view = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            if (recyclerviewHeight - (contentHeight + scrolledHeight) > view.getMeasuredHeight()) {
                showLoading(recyclerView);
            }


        }


    }

    public  void showLoading(RecyclerView recyclerView){
        ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        if(recyclerView.getChildCount()>0){
            View view=recyclerView.getChildAt(recyclerView.getChildCount()-1);
            params.bottomMargin=view.getMeasuredHeight();
            Log.e(TAG, "onScrolled: "+params.bottomMargin );
            recyclerView.setLayoutParams(params);
        }
    }
           public void hidLoading(RecyclerView recyclerView){
               ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
               if(recyclerView.getChildCount()>0){
                   View view=recyclerView.getChildAt(recyclerView.getChildCount()-1);
                   params.bottomMargin=-view.getMeasuredHeight();
                   Log.e(TAG, "onScrolled: "+params.bottomMargin );
                   recyclerView.setLayoutParams(params);
               }
           }


             public  void onLoadMore(int currentPage){};

           public interface OnLoadingListenner{
               void hidedLoading();

           }
}


