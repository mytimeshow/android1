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


    public HashMap<String, TrolleyGoods> trolleyGoodsMap = new HashMap<>();

    public TrolleyStore() {

    }

    //商品购物车本地标识 id + spec
    public String getTrolleyGoodsKey(String goodId, String storeInventoryId, String specId) {
        return goodId + "##" + storeInventoryId + "##" + specId;
    }

    public String getTrolleyGoodsKey(TrolleyGoods trolleyGoods) {
        return getTrolleyGoodsKey(trolleyGoods.goodId, trolleyGoods.storeInventoryId, trolleyGoods.specId);
    }

    //添加商品  单规格
    public int addGood(Good good, int addNum) {
        String key = getTrolleyGoodsKey(good.id, good.inventoryId, "");
        TrolleyGoods t;
        if (trolleyGoodsMap.containsKey(key)) {
            t = trolleyGoodsMap.get(key);
        } else {
            t = new TrolleyGoods(good, 0);
        }
        return addGood(t, addNum);
    }

    //添加商品  单规格 多规格
    public int addGood(TrolleyGoods trolleyGoods, int addNum) {
        String key = getTrolleyGoodsKey(trolleyGoods);
        if (!trolleyGoodsMap.containsKey(key)) {
            trolleyGoodsMap.put(key, trolleyGoods);
        }
        trolleyGoods = trolleyGoodsMap.get(key);
        trolleyGoods.add(addNum);

        if (trolleyGoods.num <= 0) {
            deleteGood(trolleyGoods);
            return 0;
        } else {
            trolleyGoods.setDeleteFlag(false);
            if (goodsList != null && !goodsList.contains(trolleyGoods)) goodsList.add(trolleyGoods);
        }
        return trolleyGoods.num;
    }

    //删除商品
    public int deleteGood(TrolleyGoods trolleyGoods) {
        String key = getTrolleyGoodsKey(trolleyGoods);
        trolleyGoods = trolleyGoodsMap.get(key);
        if (goodsList != null) goodsList.remove(trolleyGoods);
        trolleyGoods.setDeleteFlag(true);
        return 0;
    }

    //全选
    public void selectAll(boolean isSelect) {
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            t.isSelect = isSelect;
        }
    }

    //清空
    public void clearAll() {
        for (TrolleyGoods trolleyGoods : trolleyGoodsMap.values()) {
            trolleyGoods.setDeleteFlag(true);
        }
        if (goodsList != null) goodsList.clear();
    }

    //所有商品数量
    public int getGoodsBuyNum(String goodsId) {
        int num = 0;
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            if (!t.hadDeleted() && t.goodId.equals(goodsId)) num += t.num;
        }
        return num;
    }

    public int getGoodsBuyNum() {
        int num = 0;
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            if (!t.hadDeleted()&&t.isSelect) num += t.num;
        }
        return num;
    }

    public double getAllPrice(){
        double d=0;
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            if (!t.hadDeleted()&&t.isSelect) d += t.getAllPrice();
        }
        return d;
    }

    public String getAllPriceStr(){
        return String.format("￥%.2f", getAllPrice());
    }

    public double getAllPackagePrice(){
        double d=0;
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            if (!t.hadDeleted()&&t.isSelect) d += t.getAllPackagePrice();
        }
        return d;
    }

    public String getAllPackagePriceStr(){
        return String.format("￥%.2f", getAllPackagePrice());
    }

    /*
    *   用于购物车内列表显示各种商品
    * */
    transient public TrolleyFragment.TrolleyGoodsAdapter adapter = null;
    transient public List<TrolleyGoods> goodsList = null;

    public void bindGoodsAdapter(Activity activity, RecyclerView recyclerView, boolean isInStore, TrolleyStore trolleyStore) {
        if (goodsList == null) {
            goodsList = new ArrayList<>();
            for (TrolleyGoods t : trolleyGoodsMap.values()) {
                if (!t.hadDeleted()) goodsList.add(t);
            }
        }

        if (recyclerView == null) return;
        if (adapter == null) {
            adapter = new TrolleyFragment.TrolleyGoodsAdapter(goodsList, activity, trolleyStore);
            adapter.isInStore = isInStore;
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
