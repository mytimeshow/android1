package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ReduceProduct {


    /**
     * assessmentCount : 0
     * enableBuyCount : 0
     * groupList : [{"currentPrice":0,"id":"string","name":"string","restTime":0}]
     * groupTime : 0
     * labelList : [{"count":0,"id":"string","name":"string"}]
     * minPrice : 0
     * productPicIdList : ["string"]
     * productPrice : 0
     * productStoreId : string
     * productStoreInventoryId : string
     * productSubTitle : string
     * productTitle : string
     * reducePrice : 0
     * sales : 0
     * score : 0
     */

    @SerializedName("assessmentCount")
    public int assessmentCount;
    @SerializedName("enableBuyCount")
    public int enableBuyCount;
    @SerializedName("groupTime")
    public int groupTime;
    @SerializedName("minPrice")
    public double minPrice;
    @SerializedName("productPrice")
    public double productPrice;
    @SerializedName("productStoreId")
    public String productStoreId;
    @SerializedName("productStoreInventoryId")
    public String productStoreInventoryId;
    @SerializedName("productSubTitle")
    public String productSubTitle;
    @SerializedName("productTitle")
    public String productTitle;
    @SerializedName("reducePrice")
    public double reducePrice;
    @SerializedName("sales")
    public int sales;
    @SerializedName("score")
    public double score;
    @SerializedName("groupList")
    public List<GroupListBean> groupList;
    @SerializedName("labelList")
    public List<LabelListBean> labelList;
    @SerializedName("productPicIdList")
    public List<String> productPicIdList;

    public static class GroupListBean {
        /**
         * currentPrice : 0
         * id : string
         * name : string
         * restTime : 0
         */

        @SerializedName("currentPrice")
        public double currentPrice;
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("restTime")
        public int restTime;
    }

    public static class LabelListBean {
        /**
         * count : 0
         * id : string
         * name : string
         */

        @SerializedName("count")
        public int count;
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
}
