package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by ruiaa on 2017/11/13.
 */

public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
