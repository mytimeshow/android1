package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/28.
 */

public class PromoterGoodDetail {

    /**
     * commission : 0
     * id : string
     * maxPrice : 0
     * minPrice : 0
     * picId : string
     * productStoreId : string
     * rate : 0
     * sales : 0
     * subTitle : string
     * title : string
     * type : string
     */

    @SerializedName("commission")
    public double commission;
    @SerializedName("id")
    public String id;
    @SerializedName("maxPrice")
    public double maxPrice;
    @SerializedName("minPrice")
    public double minPrice;
    @SerializedName("picId")
    public String picId;
    @SerializedName("productStoreId")
    public String productStoreId;
    @SerializedName("rate")
    public double rate;
    @SerializedName("sales")
    public int sales;
    @SerializedName("subTitle")
    public String subTitle;
    @SerializedName("title")
    public String title;
    @SerializedName("type")
    public String type;
}
