package cn.czyugang.tcg.client.entity;

import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/13
 */

public class TrolleyResponse extends Response<List<Store>> {

    public HashMap<String,TrolleyStore> storeHashMap=new HashMap<>();
    public double totalPrice=0;
    public double totalRealPrice=0;
    public double marketPrice=0;
    public double takeoutPrice=0;


    public void parse(){
        if (values==null) return;
        try {
            for(Store s:data){
                TrolleyStore t=new TrolleyStore();
                t.store=s;
                storeHashMap.put(s.id,t);
            }

            //values.cartOf{data.id} 店铺内购物车信息(折扣后单价格realPrice,标签id列表（逗号分隔）)
            for(TrolleyStore t:storeHashMap.values()){
                List<TrolleyGoods> trolleyGoods= JsonParse.fromJsonInValue(values,"cartOf"+t.store.id,new JsonParse.Type(List.class,TrolleyGoods.class));
                t.mergeTrolleyGoods(trolleyGoods);
            }

            //values.totalPrice 折扣前购物车总价（包括包装费）
            totalPrice=values.optDouble("totalPrice",0);

            //values.totalRealPrice 折扣后购物车总价（包括包装费）
            totalRealPrice=values.optDouble("totalRealPrice",0);

            //values.marketPrice 商超商品总价（包括包装费）
            marketPrice=values.optDouble("marketPrice",0);

            //values.takeoutPrice 外卖商品总价（包括包装费）
            takeoutPrice=values.optDouble("takeoutPrice",0);

            //values.storeInventoryIdToAttributesDict#id={cartOf.storeInventoryId} 规格库存属性

            //values.productIdToTitleDict#id={cartOf.productId} 商品名称
            HashMap<String,String> titleMap=JsonParse.parseMap(values,"productIdToTitleDict");

            //values.productIdToPicDict#id={cartOf.productId} 商品图片
            HashMap<String,String> productIdToPicDict=JsonParse.parseMap(values,"productIdToPicDict");

            //values.storeInventoryIdToAttributeStringDict#id={cartOf.storeInventoryId} 规格库存描述
            HashMap<String,String> storeInventoryIdToAttributeStringDict=JsonParse.parseMap(values,"storeInventoryIdToAttributeStringDict");

            //values.storeInventoryIdToPackagePriceDict#id={cartOf.storeInventoryId} 包装费
            HashMap<String,Double> storeInventoryIdToPackagePriceDict=JsonParse.parseMapDouble(values,"storeInventoryIdToPackagePriceDict");

            //values.storeInventoryIdToPriceDict#id={cartOf.storeInventoryId} 单价
            HashMap<String,Double> storeInventoryIdToPriceDict=JsonParse.parseMapDouble(values,"storeInventoryIdToPriceDict");

            //values.storeInventoryIdToInventoryDict#id={cartOf.storeInventoryId} 剩余库存
            HashMap<String,Integer> storeInventoryIdToInventoryDict=JsonParse.parseMapInt(values,"storeInventoryIdToInventoryDict");

            //values.productStoreIdToTagDict#id={cartOf.productStoreId} 商品标签
            HashMap<String,String> productStoreIdToTagDict=JsonParse.parseMap(values,"productStoreIdToTagDict");

            //shoppingCartStatusDict 购物车状态字典

            //shoppingCartStatusDict 购物车状态字典

            //values.idToCustomTagValueDict#id={cartOf.id} 标签属性字符串（A+B+C）
            HashMap<String,String> idToCustomTagValueDict=JsonParse.parseMap(values,"idToCustomTagValueDict");

            //values.idToInDeliveryRangeDict{data.id} 店铺是否在当前配送范围（YES,NO）
            HashMap<String,String> idToInDeliveryRangeDict=JsonParse.parseMap(values,"idToInDeliveryRangeDict");

            for(TrolleyStore t:storeHashMap.values()){
                for(TrolleyGoods trolleyGoods:t.trolleyGoodsMap.values()){
                    trolleyGoods.name=titleMap.get(trolleyGoods.productId);
                    trolleyGoods.pic=productIdToPicDict.get(trolleyGoods.productId);
                    trolleyGoods.spec=storeInventoryIdToAttributeStringDict.get(trolleyGoods.storeInventoryId);
                    trolleyGoods.packagePrice=storeInventoryIdToPackagePriceDict.get(trolleyGoods.storeInventoryId);
                    trolleyGoods.price=storeInventoryIdToPriceDict.get(trolleyGoods.storeInventoryId);
                    trolleyGoods.inventory=storeInventoryIdToInventoryDict.get(trolleyGoods.storeInventoryId);
                }
            }


        }catch (Exception e){
            LogRui.e("parse####",e);
        }
    }
}
