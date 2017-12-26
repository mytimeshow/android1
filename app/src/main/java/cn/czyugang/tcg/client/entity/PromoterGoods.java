package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoterGoods {

    /**
     * commission : 0
     * discountPrice : 0
     * id : string
     * labels : ["string"]
     * maxPrice : 0
     * minPrice : 0
     * picId : string
     * praiseRate : string
     * rate : 0
     * sales : 0
     * title : string
     */
    @SerializedName("commission")
    public double commission;
    @SerializedName("discountPrice")
    public double discountPrice;
    @SerializedName("id")
    public String id;
    @SerializedName("maxPrice")
    public double maxPrice;
    @SerializedName("minPrice")
    public double minPrice;
    @SerializedName("picId")
    public String picId;
    @SerializedName("praiseRate")
    public String praiseRate;
    @SerializedName("rate")
    public double rate;
    @SerializedName("sales")
    public int sales;
    @SerializedName("title")
    public String title;
    @SerializedName("labels")
    public List<String> labels;
}
