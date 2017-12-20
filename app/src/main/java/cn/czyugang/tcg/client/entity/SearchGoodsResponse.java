package cn.czyugang.tcg.client.entity;

import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class SearchGoodsResponse extends Response<List<SearchGood>> {

    public void parse() {
        if (values == null) return;
        try {

            //values.productIdToPicDict#id={data.productId} 图片
            HashMap<String, String> picMap = JsonParse.parseMap(values, "productIdToPicDict");

            //values.productIdToTitleDict#id={data.id} 标题
            HashMap<String, String> titleMap = JsonParse.parseMap(values, "productIdToTitleDict");
            //values.idToPriceDict#id={data.id} 价格
            HashMap<String, Double> priceMap = JsonParse.parseMapDouble(values, "idToPriceDict");
            //values.idToSalesDict#id={data.id} 已售
            HashMap<String, Integer> saleMap = JsonParse.parseMapInt(values, "idToSalesDict");
            //values.idToGoodRateDict#id={data.id} 好评率
            HashMap<String, Integer> goodRateMap = JsonParse.parseMapInt(values, "idToGoodRateDict");
            //values.idToIsCollectionDict 用户是否收藏商品（YES,NO）
            HashMap<String, String> collectionMap = JsonParse.parseMap(values, "idToIsCollectionDict");
            //values.idToStatusDict#id={data.id} 商品状态   name=接受预订中 name=商家休息中
            HashMap<String, String> statusMap = JsonParse.parseMap(values, "idToStatusDict");
            //values.idToStatusKeyDict                      name=BOOK       name=STORE_IN_REST
            HashMap<String, String> statusKeyMap = JsonParse.parseMap(values, "idToStatusKeyDict");
            //statusDict 商品状态字典     id=NORMAL  id=STORE_IN_REST  id=BOOK


            //values.storeIdToDeliveryTimeDict#id={data.storeId} 配送时间
            HashMap<String, Double> deliveryTimeMap = JsonParse.parseMapDouble(values, "storeIdToDeliveryTimeDict");
            //values.storeIdToDistanceDict#id={data.storeId} 距离
            HashMap<String, Double> distanceMap = JsonParse.parseMapDouble(values, "storeIdToDistanceDict");
            //values.storeIdToDeliveryWayDict#id={data.storeId} 配送方式  name=同城鸽专送
            HashMap<String, String> deliveryWayMap = JsonParse.parseMap(values, "storeIdToDeliveryWayDict");
            //values.storeIdToDeliveryWayKeyDict                            name=YES  name=NO

            //values.menuTagList 聚合菜单标签（商品标签）
            //values.menuServiceTagList 聚合菜单标签（服务标签）
            //values.menuClassifyDictList 聚合菜单类目
            //values.menuBrandDictList 聚合菜单品牌
            //values.menuAttributeList 聚合菜单属性（自然，规格）

            //values.tagOf{data.id} 商品标签组

            for(SearchGood good:data){
                good.pic=picMap.get(good.productId);
                good.title=titleMap.get(good.id);
                good.price=priceMap.get(good.id);
                good.sales=saleMap.get(good.id);
                good.goodRate=goodRateMap.get(good.id);
                good.collected=collectionMap.get(good.id).equals("YES");
                good.status=statusMap.get(good.id);

                good.deliveryTime=deliveryTimeMap.get(good.storeId);
                good.deliveryDistance=distanceMap.get(good.storeId);
                good.deliveryWay=deliveryWayMap.get(good.storeId);
            }

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }
}
