package cn.czyugang.tcg.client.entity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.modules.common.ImgAdapter;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/14
 */

public class OrderPreSettleResponse extends Response<List<Store>> {

    transient public Address address = null;
    transient public Map<String, StoreMoreInfo> moreInfoHashMap = new HashMap<>();//store.id
    transient public Map<String, TrolleyGoods> trolleyGoodsMap = new HashMap<>();//TrolleyGoods.trolleyId

    public double totalPrice = 0;// 折扣前购物车总价（包括包装费）
    public double totalRealPrice = 0;//折扣后购物车总价（包括包装费）
    public double marketPrice = 0;// 商超商品总价（包括包装费）
    public double takeoutPrice = 0;// 外卖商品总价（包括包装费）

    public void parse() {
        for (Store store : data) {
            moreInfoHashMap.put(store.id, new StoreMoreInfo(store.id));
        }
        try {
            if (values == null) return;

            //地址
            String addressStr = values.optString("userAddress");
            if (!addressStr.equals("")) address = JsonParse.fromJson(addressStr, Address.class);

            //总价
            totalPrice = values.getDouble("totalPrice");
            totalRealPrice = values.getDouble("totalRealPrice");
            marketPrice = values.getDouble("marketPrice");
            takeoutPrice = values.getDouble("takeoutPrice");

            //购物车
            for (Store store : data) {
                String goodsStr = values.optString("cartOf" + store.id);
                if (!goodsStr.equals("")) {
                    List<TrolleyGoods> trolleyGoodsList = JsonParse.fromJson(goodsStr, new JsonParse.Type(List.class, TrolleyGoods.class));
                    getMoreInfo(store).trolleyGoodsList = trolleyGoodsList;
                    for (TrolleyGoods t : trolleyGoodsList) {
                        trolleyGoodsMap.put(t.trolleyId, t);
                    }
                }
            }

            parseDelivery();

            parseGoodsImgTitle();

            parseSpecAndTag();

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }

    }

    private void parseDelivery() throws JSONException {
        //values.idToInDeliveryRangeDict{data.id} 店铺是否在当前配送范围（YES,NO）
        JSONArray jsonArray = values.optJSONArray("idToInDeliveryRangeDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                getMoreInfo(jsonObject.getString("id")).isInDeliveryRange = jsonObject.optString("name").equals("YES");
            }
        }

        //values.storeIdToDeliveryTypeDict#id={data.id} 店铺内配送方式     name=PLATFORM,FETCH
        //values.storeIdToDeliveryPriceDict#id={data.id} 配送费
        //values.storeIdToPlatformDeliveryLeftSecondDict#id={data.id} 各店铺剩余平台配送时间（配送类型为平台配送且少于10分钟时返回,单位为秒）
        jsonArray = values.optJSONArray("storeIdToDeliveryTypeDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                StoreMoreInfo moreInfo = getMoreInfo(jsonObject.getString("id"));
                moreInfo.deliveryType = jsonObject.optString("name");
                moreInfo.selectedDeliveryWay = StoreMoreInfo.transferDeliveryType(moreInfo.deliveryType.split(",")[0]);
            }
        }
        jsonArray = values.optJSONArray("storeIdToDeliveryPriceDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                getMoreInfo(jsonObject.getString("id")).deliveryPrice = jsonObject.optDouble("name", 0);
            }
        }
        jsonArray = values.optJSONArray("storeIdToPlatformDeliveryLeftSecondDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                getMoreInfo(jsonObject.getString("id")).platformDeliveryRemainTime = jsonObject.optLong("name", -1);
            }
        }

        //values.deliveryTimeOf{data.id} 可配送时间列表
        for (Store store : data) {
            String deliveryTimeStr = values.optString("deliveryTimeOf" + store.id);
            if (!deliveryTimeStr.equals("")) {
                StoreMoreInfo moreInfo = getMoreInfo(store);
                moreInfo.deliveryTimeList = JsonParse.fromJson(deliveryTimeStr, new JsonParse.Type(List.class, DeliveryTime.class));
                moreInfo.selectedDeliveryTime = moreInfo.deliveryTimeList.get(0).date;
            }
        }


    }

    private void parseGoodsImgTitle() throws JSONException {
        //productIdToTitleDict#id={cartOf.productId} 商品名称
        //productIdToPicDict#id={cartOf.productId} 商品图片
        HashMap<String, String> goodsTitleMap = new HashMap<>();
        JSONArray jsonArray = values.optJSONArray("productIdToTitleDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                goodsTitleMap.put(jsonObject.getString("id"), jsonObject.getString("name"));
            }
        }
        HashMap<String, String> goodsImgMap = new HashMap<>();
        jsonArray = values.optJSONArray("productIdToPicDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                goodsImgMap.put(jsonObject.getString("id"), jsonObject.getString("name"));
            }
        }
        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            if (goodsTitleMap.containsKey(t.productId)) t.name = goodsTitleMap.get(t.productId);
            if (goodsImgMap.containsKey(t.productId)) t.pic = goodsImgMap.get(t.productId);
        }
    }

    private void parseSpecAndTag() throws JSONException {
        //storeInventoryIdToAttributeStringDict  name=黑色+M
        //values.storeInventoryIdToAttributeStringDict#id={cartOf.storeInventoryId} 规格库存描述
        HashMap<String, String> specMap = new HashMap<>();
        JSONArray jsonArray = values.optJSONArray("storeInventoryIdToAttributeStringDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                specMap.put(jsonObject.getString("id"), jsonObject.getString("name").replaceAll("\\+", " "));
            }
        }

        //idToCustomTagValueDict        name=连衣裙+null+null
        //values.idToCustomTagValueDict#id={cartOf.id} 标签属性字符串（A+B+C）
        HashMap<String, String> cusSpecMap = new HashMap<>();
        jsonArray = values.optJSONArray("idToCustomTagValueDict");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                cusSpecMap.put(jsonObject.getString("id"),
                        jsonObject.getString("name").replaceAll("\\+null", "").replaceAll("\\+", " "));
            }
        }

        for (TrolleyGoods t : trolleyGoodsMap.values()) {
            String spec = "";
            if (specMap.containsKey(t.storeInventoryId))
                spec = spec + specMap.get(t.storeInventoryId) + " ";
            if (cusSpecMap.containsKey(t.trolleyId))
                spec = spec + cusSpecMap.get(t.trolleyId);
            t.spec = spec;

            //values.tagOfShoppingCart{cartOf.id} 购物车商品对应所有标签(标签名称name)
            jsonArray = values.optJSONArray("tagOfShoppingCart" + t.trolleyId);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (t.label == null) t.label = new ArrayList<>();
                    t.label.add(jsonObject.optString("name"));
                }
            }
        }
    }

    public void setDeliveryTimeAndWay(Map<String, String> timeMap, Map<String, String> wayMap) {
        for(StoreMoreInfo moreInfo:moreInfoHashMap.values()){
            if (timeMap.containsKey(moreInfo.storeId)) moreInfo.selectedDeliveryTime=timeMap.get(moreInfo.storeId);
            if (wayMap.containsKey(moreInfo.storeId)) moreInfo.selectedDeliveryWay=wayMap.get(moreInfo.storeId);
        }
    }

    public boolean isAllInDeliveryRange() {
        for (StoreMoreInfo moreInfo : moreInfoHashMap.values()) {
            if (!moreInfo.isInDeliveryRange) return false;
        }
        return true;
    }

    public StoreMoreInfo getMoreInfo(Store store) {
        return moreInfoHashMap.get(store.id);
    }

    public StoreMoreInfo getMoreInfo(String storeId) {
        return moreInfoHashMap.get(storeId);
    }

    public static class StoreMoreInfo {
        public String storeId = "";
        public List<TrolleyGoods> trolleyGoodsList;
        public List<String> imgList = new ArrayList<>();
        private ImgAdapter imgAdapter = null;

        public boolean isInDeliveryRange = true;

        private String deliveryType = "";//PLATFORM 平台配送     STORE 商家配送      FETCH  自提
        public double deliveryPrice = 0;
        public long platformDeliveryRemainTime = -1;
        public List<DeliveryTime> deliveryTimeList;
        public String selectedDeliveryWay = "";
        public String selectedDeliveryTime = "";
        public String noteToStore="";

        public StoreMoreInfo(String storeId) {
            this.storeId = storeId;
        }

        public int getGoodsNum() {
            if (trolleyGoodsList == null) return 0;
            int num = 0;
            for (TrolleyGoods t : trolleyGoodsList) {
                num += t.num;
            }
            return num;
        }

        public void bindImgAdapter(Activity activity, RecyclerView recyclerView) {
            if (imgAdapter == null) {
                imgAdapter = new ImgAdapter(imgList, activity);
                imgAdapter.setSizeRes(R.dimen.dp_60);
                for (TrolleyGoods t : trolleyGoodsList) {
                    imgList.add(t.pic);
                }
            }
            if (recyclerView.getLayoutManager() == null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            }
            recyclerView.setAdapter(imgAdapter);
        }

        public boolean arrowDeliveryPlatform() {
            return deliveryType.contains("PLATFORM");
        }

        public boolean arrowDeliveryStore() {
            return deliveryType.contains("STORE");
        }

        public boolean arrowDeliveryFETCH() {
            return deliveryType.contains("FETCH");
        }

        public static String transferDeliveryType(String type) {
            switch (type) {
                case "PLATFORM":
                    return "平台配送";
                case "平台配送":
                    return "PLATFORM";
                case "STORE":
                    return "商家配送";
                case "商家配送":
                    return "STORE";
                case "FETCH":
                    return "自提";
                case "自提":
                    return "FETCH";
            }
            return "";
        }
    }

    public static class DeliveryTime {
        public String date;
        public double price;
    }
}
