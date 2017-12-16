package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ruiaa
 * @date 2017/12/8
 */

public class TrolleyGoods {

    @SerializedName("storeId")
    public String storeId;
    @SerializedName("id")
    public String trolleyId = "";
    @SerializedName("isSettle")
    public String isSettle;//是否已结算
    @SerializedName("productId")
    public String productId;
    @SerializedName("productInventoryId")
    public String productInventoryId;
    @SerializedName("productType")
    public String productType;
    @SerializedName("realPrice")
    public double realPrice;


    @SerializedName("productStoreId")
    public String goodId = "";

    public String name = "";
    public String pic="";       //下单时
    public String spec = "";
    @SerializedName("tagList")
    public String specId = "";
    @SerializedName("storeInventoryId")
    public String storeInventoryId = "";
    transient public List<String> label=null;

    @SerializedName("price")
    public double price = 0;
    @SerializedName("packagePrice")
    public double packagePrice = 0;
    @SerializedName("number")
    public int num = 0;

    @SerializedName("status")
    public String status = "NORMAL";       //NORMAL:正常,INVALID:失效，SOLD:售罄
    @SerializedName("deleteFlag")
    public String deleteFlag = "NORMAL";       //删除标识,NORMAL正常DELETED删除


    public boolean isSelect = true;

    public TrolleyGoods(String goodId, String name, String spec, String specId, String storeInventoryId,
                        double price, double packagePrice, int num) {
        this.goodId = goodId;
        this.name = name;
        this.spec = spec;
        this.specId = specId;
        this.storeInventoryId = storeInventoryId;
        this.price = price;
        this.packagePrice = packagePrice;
        this.num = num;
    }

    public TrolleyGoods(Good good, int num) {
        this.num = num;
        goodId = good.id;
        name = good.title;
        price = good.showPrice;
        packagePrice = good.packagePrice;
        storeInventoryId = good.inventoryId;
    }

    public String getRealPriceStr(){
        return String.format("￥%.2f", realPrice);
    }

    public String getPriceStr() {
        return String.format("￥%.2f", price);
    }

    public String getPackagePriceStr() {
        return String.format("￥%.2f", packagePrice);
    }

    public double getAllPrice() {
        return ((double) num) * price;
    }

    public double getAllPackagePrice() {
        return ((double) num) * packagePrice;
    }

    public void setDeleteFlag(boolean delete) {
        deleteFlag = delete ? "DELETED" : "NORMAL";
        if (delete) num = 0;
    }

    public boolean hadDeleted() {
        return deleteFlag.equals("DELETED");
    }

    void add(int addNum) {
        num += addNum;
        if (num<=0) deleteFlag="DELETED";
    }

    public void setInfoFromSyncToLocal(TrolleyStore trolleyStore){
        List<Long> ids=new ArrayList<>();
        for (String id : specId.split(",")) {
            ids.add(Long.valueOf(id));
        }
        Collections.sort(ids,(o1, o2) -> {
            if (o1>o2) return 1;
            if (o1<o2) return -1;
            return 0;
        });
        String idsStr=ids.toString().replaceAll(" ","");
        specId=idsStr.substring(1,idsStr.length()-1);

        String key=trolleyStore.getTrolleyGoodsKey(this);
        if (!trolleyStore.trolleyGoodsMap.containsKey(key)) return;
        TrolleyGoods local=trolleyStore.trolleyGoodsMap.get(key);
        /*
    "deleteFlag": "string",
    "id": "string",
    "isSettle": "string",
    "number": 0,
    "packagePrice": 0,
    "price": 0,
    "productId": "string",
    "productInventoryId": "string",
    "productStoreId": "string",
    "productType": "string",
    "realPrice": 0,
    "status": "string",
    "storeId": "string",
    "storeInventoryId": "string",
    "tagList": "string",
        * */
        local.deleteFlag=deleteFlag;
        local.trolleyId=trolleyId;
        local.isSettle=isSettle;
        local.num=num;
        local.packagePrice=packagePrice;
        local.price=price;
        local.productId=productId;
        local.productInventoryId=productInventoryId;
        local.goodId=goodId;
        local.productType=productType;
        local.realPrice=realPrice;
        local.status=status;
        local.storeId=storeId;
        local.storeInventoryId=storeInventoryId;
        local.specId=specId;
    }
}
