package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class GroupDetail {

    /**
     * activityProductId : string
     * assessmentCount : 0
     * description : string
     * groupList : [{"currentPrice":0,"id":"string","name":"string","restTime":0}]
     * groupTime : 0
     * historyList : [{"createTime":"2017-12-27T09:34:32.102Z","name":"string","type":"string"}]
     * id : string
     * inventory : 0
     * memberCount : 0
     * minPrice : 0
     * productPicId : string
     * productPrice : 0
     * productSubTitle : string
     * productTitle : string
     * reducePrice : 0
     * restDiscount : 0
     * restTime : 0
     * sales : 0
     * status : string
     */

    @SerializedName("activityProductId")
    public String activityProductId;
    @SerializedName("assessmentCount")
    public int assessmentCount;
    @SerializedName("description")
    public String description;
    @SerializedName("groupTime")
    public int groupTime;
    @SerializedName("id")
    public String id;
    @SerializedName("inventory")
    public int inventory;
    @SerializedName("memberCount")
    public int memberCount;
    @SerializedName("minPrice")
    public double minPrice;
    @SerializedName("productPicId")
    public String productPicId;
    @SerializedName("productPrice")
    public double productPrice;
    @SerializedName("productSubTitle")
    public String productSubTitle;
    @SerializedName("productTitle")
    public String productTitle;
    @SerializedName("reducePrice")
    public double reducePrice;
    @SerializedName("restDiscount")
    public double restDiscount;
    @SerializedName("restTime")
    public int restTime;
    @SerializedName("sales")
    public int sales;
    @SerializedName("status")
    public String status;
    @SerializedName("groupList")
    public List<GroupListBean> groupList;
    @SerializedName("historyList")
    public List<HistoryListBean> historyList;

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

    public static class HistoryListBean {
        /**
         * createTime : 2017-12-27T09:34:32.102Z
         * name : string
         * type : string
         */

        @SerializedName("createTime")
        public String createTime;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public String type;
    }
}
