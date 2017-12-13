package cn.czyugang.tcg.client.entity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.modules.entry.fragment.TrolleyFragment;

/**
 * @author ruiaa
 * @date 2017/12/7
 */

public class TrolleyStore {


    private HashMap<String, TrolleyGoods> trolleyGoodsMap = new HashMap<>();

    public TrolleyStore() {
        trolleyGoodsMap.put("8888##kkkkk", new TrolleyGoods(new Good(), "kkkkk", 9));
        trolleyGoodsMap.put("8878##kkkkk", new TrolleyGoods(new Good(), "kkkkk", 9));
    }

    //商品购物车本地标识 id + spec
    public String getTrolleyGoodsKey(Good good, String spec) {
        if (spec == null) spec = "";
        return good.id + "##" + spec;
    }

    //添加商品
    public int addGood(Good good, String spec, int addNum) {
        String key = getTrolleyGoodsKey(good, spec);
        if (trolleyGoodsMap.containsKey(key)) {
            trolleyGoodsMap.get(key).add(addNum);
            if (trolleyGoodsMap.get(key).num <= 0) {
                if (goodsList != null) goodsList.remove(trolleyGoodsMap.get(key));
                trolleyGoodsMap.remove(key);
                return 0;
            }
            return trolleyGoodsMap.get(key).num;
        } else {
            if (addNum <= 0) return addNum;
            TrolleyGoods t = new TrolleyGoods(good, spec, addNum);
            trolleyGoodsMap.put(key, t);
            if (goodsList != null) goodsList.add(t);
            return addNum;
        }
    }

    //全选
    public void selectAll(boolean isSelect) {
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            t.isSelect = isSelect;
        }
    }

    //清空
    public void clear(TrolleyGoods t) {
        if (t == null) return;
        trolleyGoodsMap.remove(getTrolleyGoodsKey(t.good, t.spec));
        if (goodsList != null) goodsList.remove(t);
    }

    public void clearAll() {
        trolleyGoodsMap.clear();
        goodsList.clear();
    }

    public int getGoodsBuyNum(String goodsId) {
        int num = 0;
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            if (t.good.id.equals(goodsId)) num += t.num;
        }
        return num;
    }

    /*
    *   用于购物车内列表显示各种商品
    * */
    transient public TrolleyFragment.TrolleyGoodsAdapter adapter = null;
    transient public List<TrolleyGoods> goodsList = null;

    public void bindGoodsAdapter(Activity activity, RecyclerView recyclerView, boolean isStoreTrolley) {
        if (goodsList == null) {
            goodsList = new ArrayList<>();
            goodsList.addAll(trolleyGoodsMap.values());
        }

        if (recyclerView == null) return;
        if (adapter == null) {
            adapter = new TrolleyFragment.TrolleyGoodsAdapter(goodsList, activity);
            adapter.isStoreTrolley = isStoreTrolley;
        }
        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }


    /*
    *   用于全站购物车中的RecyclerView viewType  商家，失效商家，清空失效商家，广告轮播图
    * */
    //R.layout.item_trolley_store_ad
    //R.layout.item_trolley_store_invalid_clear
    //R.layout.item_trolley_store_invalid_title
    transient public int viewType = R.layout.item_trolley_store;

    public TrolleyStore setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }
}
