package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2018/1/4
 */

public class QrcodeActRespose extends Response<List<Good>> {
    public String activityId = "";
    public String activityName = "";

    public void parse() {
        if (values == null) return;
        try {
            //values.qrcodeProductStoreInfoDTO_skuTypeDict 规格类型字典
            //values.labelBaseDTO_typeDict 标签类型字典

            //values.attributeList#productId={productStoreOf.productId} 商品属性(属性名name)

            //values.attributeValueList#baseId={attributeList.id} 商品属性值(属性值value)

            //values.inventoryOfProductStore{productStoreOf.id} 商品剩余库存及售价（售价取第一个的price,关联inventoryId到productInventoryList.id从而确定用户所选择库存）
            //库存id
            for (Good g:data){
                JSONArray jsonArray=values.optJSONArray("inventoryOfProductStore"+g.id);
                if (jsonArray!=null&&jsonArray.length()!=0) {
                    JSONObject jsonObject=jsonArray.optJSONObject(0);
                    if (jsonObject==null) continue;
                    g.inventoryId=jsonObject.optString("id");
                    g.packagePrice=jsonObject.optDouble("packagePrice");
                    g.showPrice=jsonObject.optDouble("price");
                    g.showRemain=jsonObject.optInt("currentInventory");
                }
            }

            //values.productInventoryList#productId={productStoreOf.productId} 商品总库存信息(attributes用于组装店铺库存)

            //values.customTagOfProduct{productStoreOf.productId} 商品标签属性

            //values.customTagValueOfCustomTag{customTagOfProduct.id} 商品标签属性值


            //activityId=943757002256068608
            activityId = values.optString("activityId");
            //activityName=20171221Test001
            activityName = values.optString("activityName");

            //labelBaseDTO_typeDict
            //qrcodeProductStoreInfoDTO_skuTypeDict

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }
}
