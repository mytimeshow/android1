package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/23
 */

public class RecordResponse extends Response<List<Record>> {

    //values.storeInfoBaseList#id = {data.objectId}（[店铺]店铺基础信息列表-店铺头像，店铺名称）
    //values.storeInfoDetailList#baseId = {data.objectId}（[店铺]店铺详情信息列表-店铺地理位置）
    //values.startOf{data.objectId}（[店铺]店铺星级数（非本期内容））
    //values.activityOf{data.objectId}（[店铺]店铺活动数（非本期内容））
    //values.productStoreInventoryList#productId = {data.objectId}（[商品]收藏商品规格信息-商品价格）
    //values.productInfoList#id = {data.objectId}（[商品]商品基础信息-商品名称）
    //values.storeInfoBaseList#id = {productStoreInventory.productStoreId}（[商品]店铺基础信息列表-店铺休息中）
    //values.productPicRelationList#productId = {productInfo.id}（[商品]商品首张图片列表）
    //values.originalPriceOf{data.object}（[商品]商品原始价格）
    //values.isDiscountOf{data.object}（[商品]商品是否打折;是-YES,否-NO）
    //values.infoRecordList#id = {data.objectId}（资讯信息列表）


    public void parseStoreCollection() {
        if (values == null) return;
        try {

            JSONObject activityMap = values.optJSONObject("activityMap");

            JSONObject startMap = values.optJSONObject("startMap");

            HashMap<String, Store> storeMap = new HashMap<>();
            JSONArray jsonArray = values.optJSONArray("storeInfoBase");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    Store store = JsonParse.fromJson(jsonArray.getString(i), Store.class);
                    storeMap.put(store.id, store);
                }
            }

            for (Record record : data) {
                if (activityMap != null)
                    record.activityNum = activityMap.optInt("activityOf" + record.objectId, 0);
                if (startMap != null)
                    record.starScore = startMap.optDouble("startOf" + record.objectId, 0);
                if (storeMap.containsKey(record.objectId))
                    record.store = storeMap.get(record.objectId);
            }

        } catch (Exception e) {
            LogRui.e("parseStoreCollection####", e);
        }
    }


    public void parseGoodCollection() {

    }


    public void parseInformCollection() {
        if (values == null) return;
        try {
            HashMap<String, Inform> informMap = new HashMap<>();
            JSONArray jsonArray = values.optJSONArray("infoRecordList");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    Inform inform = JsonParse.fromJson(jsonArray.optString(i), Inform.class);
                    informMap.put(inform.id, inform);
                }
            }

            for (Record record : data) {
                if (informMap.containsKey(record.objectId))
                    record.inform = informMap.get(record.objectId);
            }

        } catch (Exception e) {
            LogRui.e("parseInformCollection####", e);
        }
    }


    public void parseStoreFootmark() {

    }

    public void parseGoodFootmark() {

    }


    public void parseInformFootmark() {

    }
}
