package cn.czyugang.tcg.client.entity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.modules.entry.fragment.TrolleyFragment;

/**
 * @author ruiaa
 * @date 2017/12/7
 */

public class TrolleyStore {

    //R.layout.item_trolley_store_ad
    //R.layout.item_trolley_store_invalid_clear
    //R.layout.item_trolley_store_invalid_title
    transient public int viewType=R.layout.item_trolley_store;

    List<TrolleyGoods> goodsList = new ArrayList<>();

    public TrolleyStore(){
        goodsList.add(new TrolleyGoods());
        goodsList.add(new TrolleyGoods());
        goodsList.add(new TrolleyGoods());
        goodsList.add(new TrolleyGoods());
        goodsList.add(new TrolleyGoods());
    }

    private TrolleyFragment.TrolleyGoodsAdapter adapter = null;

    public void bindGoodsAdapter(Activity activity, RecyclerView recyclerView, boolean isStoreTrolley) {
        if (recyclerView == null) return;
        if (adapter == null) {
            adapter = new TrolleyFragment.TrolleyGoodsAdapter(goodsList, activity);
            adapter.isStoreTrolley = isStoreTrolley;
        }
        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }

    public TrolleyStore setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }
}
