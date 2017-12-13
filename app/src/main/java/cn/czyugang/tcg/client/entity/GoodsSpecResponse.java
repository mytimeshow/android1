package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/13
 */

public class GoodsSpecResponse extends Response<List<GoodsSpec>> {

    private Map<String, Integer> specToInventoryMap = new HashMap<>();
    private Map<String, Double> specToPriceMap = new HashMap<>();
    private Map<String,String> specToInventoryIdMap =new HashMap<>();

    public void parse() {
        try {
            if (values == null) {
                LogRui.e("parse####  value is null");
                return;
            }

            //获取属性值
            for (GoodsSpec spec : data) {
                spec.goodsSpecValueList = JsonParse.fromJson(values.getString("attributeValueOf" + spec.id),
                        new JsonParse.Type(List.class, GoodsSpec.GoodsSpecValue.class));
                spec.initLabelList();
            }

            // id -->  库存
            Map<String, Integer> inventoryMap = new HashMap<>();
            JSONArray jsonArray=values.getJSONArray("storeInventoryIdToInventoryDict");
            for(int i=0,size=jsonArray.length();i<size;i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                inventoryMap.put(jsonObject.getString("id"),jsonObject.getInt("name"));
            }

            // id --> 价格
            Map<String, Double> priceMap = new HashMap<>();
            jsonArray=values.getJSONArray("storeInventoryIdToPriceDict");
            for(int i=0,size=jsonArray.length();i<size;i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                priceMap.put(jsonObject.getString("id"),jsonObject.getDouble("name"));
            }

            // 规格值 -->  库存，价格，id
            jsonArray=values.getJSONArray("attributesToStoreInventoryIdDict");
            Map<String,String> specKeyValueMap=new HashMap<>();
            for(int i=0,size=jsonArray.length();i<size;i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String specKey=transferSpecKey(jsonObject.getString("id"),specKeyValueMap);
                String valueId=jsonObject.getString("name");
                specToInventoryMap.put(specKey,inventoryMap.get(valueId));
                specToPriceMap.put(specKey,priceMap.get(valueId));
                specToInventoryIdMap.put(specKey,valueId);
            }

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }

    //id=940125959103897608:940125959129063432,940125959103897607:940125959129063431,940125959103897606:940125959129063429
    private String transferSpecKey(String id,Map<String,String> specKeyValueMap) {
        String[] keys = id.split(":|,");
        for (int i = 0, size = keys.length; i < size; i = i + 2) {
            specKeyValueMap.put(keys[i],keys[i+1]);
        }
        StringBuilder builder=new StringBuilder();
        for(GoodsSpec goodsSpec:data){
            if (specKeyValueMap.containsKey(goodsSpec.id)){
                goodsSpec.isPowerfulToPriceSpec=true;
                builder.append("###");
                String valueId=specKeyValueMap.get(goodsSpec.id);
                for(GoodsSpec.GoodsSpecValue goodsSpecValue:goodsSpec.goodsSpecValueList){
                    if (goodsSpecValue.id.equals(valueId)){
                        builder.append(goodsSpecValue.value);
                        break;
                    }
                }
            }
        }
        return builder.toString();
    }

    private String getPowerfulToPriceSpec() {
        StringBuilder builder = new StringBuilder();
        for (GoodsSpec spec : data) {
            if (!spec.isPowerfulToPriceSpec) continue;
            builder.append("###");
            builder.append(spec.selectLabel);
        }
        return builder.toString();
    }

    public String getShowSelectSpec() {
        StringBuilder builder = new StringBuilder();
        builder.append("已选：");
        boolean isFirst = true;
        for (GoodsSpec spec : data) {
            if (!isFirst) {
                builder.append("/");
            }
            isFirst = false;
            builder.append(spec.selectLabel);
        }
        return builder.toString();
    }

    public void resetSelectLabel(){
        for (GoodsSpec spec:data){
            spec.selectLabel=spec.labelList.get(0);
        }
    }

    public String getInventoryId(){
        return specToInventoryIdMap.get(getPowerfulToPriceSpec());
    }

    public int getInventory(){
        return specToInventoryMap.get(getPowerfulToPriceSpec());
    }

    public double getPrice(){
        return specToPriceMap.get(getPowerfulToPriceSpec());
    }

    public String getPriceStr(){
        return String.format("￥%.2f", getPrice());
    }

    public TrolleyGoods buy(Good good){
        String powerfulToPriceSpec=getPowerfulToPriceSpec();
        TrolleyGoods trolleyGoods=new TrolleyGoods();
        trolleyGoods.good=good;
        trolleyGoods.spec=getShowSelectSpec();
        trolleyGoods.price=getPrice();
        trolleyGoods.packagePrice=good.packagePrice;
        trolleyGoods.storeInventoryId=getInventoryId();
        return trolleyGoods;
    }
}
