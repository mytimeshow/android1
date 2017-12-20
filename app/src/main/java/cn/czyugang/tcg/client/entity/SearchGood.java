package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class SearchGood {

    /**
     * bookType : string
     * createTime : 2017-12-20T08:31:01.203Z
     * deleteFlag : string
     * disparage : 0
     * id : string
     * order : 0
     * praise : 0
     * productId : string
     * qrCodeUrl : string
     * recycled : string
     * saleTimeType : string
     * sales : 0
     * score : 0
     * status : string
     * statusUpdateTime : 2017-12-20T08:31:01.203Z
     * storeId : string
     * updateTime : 2017-12-20T08:31:01.203Z
     */

    @SerializedName("bookType")
    public String bookType;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("disparage")
    public int disparage;
    @SerializedName("id")
    public String id;
    @SerializedName("order")
    public int order;
    @SerializedName("praise")
    public int praise;
    @SerializedName("productId")
    public String productId;
    @SerializedName("qrCodeUrl")
    public String qrCodeUrl;
    @SerializedName("recycled")
    public String recycled;
    @SerializedName("saleTimeType")
    public String saleTimeType;
    @SerializedName("sales")
    public int sales;
    @SerializedName("score")
    public int score;
    @SerializedName("status")
    public String status;
    @SerializedName("statusUpdateTime")
    public String statusUpdateTime;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("updateTime")
    public String updateTime;

    public String title="";
    public double price=0;
    public double goodRate=0;
    public boolean collected=false;
    public double deliveryTime=0;
    public double deliveryDistance=0;
    public String deliveryWay="";

    public String pic = "";
}
