package cn.czyugang.tcg.client.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class SearchStoreResponse extends Response<List<SearchStore>> {

    public void parse() {
        if (values == null) return;
        try {
            //values.storeIdToStorePicDict#id={data.storeId} 店铺图片
            HashMap<String, String> picMap = JsonParse.parseMap(values, "storeIdToStorePicDict");
            //values.storeIdToDeliveryWayDict#id={data.storeId} 店铺配送方式（是否平台配送）
            HashMap<String, String> deliveryWayMap = JsonParse.parseMap(values, "storeIdToDeliveryWayDict");
            //values.storeIdToStartDeliveryPriceDict#id={data.storeId} 起送费
            HashMap<String, Double> startDeliveryPriceMap = JsonParse.parseMapDouble(values, "storeIdToStartDeliveryPriceDict");
            //values.storeIdToDeliveryPriceDict#id={data.storeId} 配送费
            HashMap<String, Double> deliveryPriceMap = JsonParse.parseMapDouble(values, "storeIdToDeliveryPriceDict");
            //values.storeIdToDistanceDict#id={data.storeId} 距离
            HashMap<String, Double> distanceMap = JsonParse.parseMapDouble(values, "storeIdToDistanceDict");
            //values.storeIdToDeliveryTimeDict#id={data.storeId} 送达时间
            HashMap<String, Double> deliveryTimeMap = JsonParse.parseMapDouble(values, "storeIdToDeliveryTimeDict");
            //values.storeIdToScoreDict#id={data.storeId} 评分
            HashMap<String, Integer> scoreMap = JsonParse.parseMapInt(values, "storeIdToScoreDict");
            //values.storeIdToIsNewDict#id={data.storeId} 是否新店（YES,NO）
            HashMap<String, String> isNewMap = JsonParse.parseMap(values, "storeIdToIsNewDict");
            //values.storeIdToStatusDict#id={data.storeId} 店铺状态     id=STORE_IN_REST name=商家休息中       id=BOOK  name=接受预订中
            HashMap<String, String> statusMap = JsonParse.parseMap(values, "storeIdToStatusDict");

            //values.productStoreOf{data.storeId} 店铺商品信息
            //values.productStoreOf_productIdToTitleDict#id={productStoreOf.productId} 商品名称
            HashMap<String, String> goodsTitleMap = JsonParse.parseMap(values, "productStoreOf_productIdToTitleDict");
            //values.productStoreOf_productIdToPicDict#id={productStoreOf.productId} 商品图片
            HashMap<String, String> goodsPicMap = JsonParse.parseMap(values, "productStoreOf_productIdToPicDict");
            //values.productStoreOf_idToPriceDict#id={productStoreOf.id} 商品价格
            HashMap<String, Double> goodsPriceMap = JsonParse.parseMapDouble(values, "productStoreOf_idToPriceDict");

            //values.menuClassifyList 店铺分类
            //values.menuAreaList 店铺地区
            //values.menuTagList 店铺标签
            //values.menuServiceTagList 授权服务标签
            //values.menuDeliveryWayList 配送方式

            //values.statusDict 状态字典
            //id=STORE_IN_REST name=商家休息中       id=BOOK  name=接受预订中

            //values.deliveryWayDict 配送方式字典
            //

            for (SearchStore store : data) {
                store.pic = picMap.get(store.storeId);
                store.deliveryWay = deliveryWayMap.get(store.storeId);
                store.startDeliveryPrice = startDeliveryPriceMap.get(store.storeId);
                store.deliveryPrice = deliveryPriceMap.get(store.storeId);
                store.distance = distanceMap.get(store.storeId);
                store.deliveryTime = deliveryTimeMap.get(store.storeId);
                store.score = scoreMap.get(store.storeId);
                store.isNew = isNewMap.get(store.storeId);
                store.status = statusMap.get(store.storeId);

                store.goodList = JsonParse.fromJsonInValue(values, "productStoreOf" + store.storeId,
                        new JsonParse.Type(List.class, SearchGood.class), new ArrayList<>());
                for(SearchGood good:store.goodList){
                    good.title=goodsTitleMap.get(good.productId);
                    good.pic=goodsPicMap.get(good.productId);
                    good.price=goodsPriceMap.get(good.id);
                }
            }

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }

}
