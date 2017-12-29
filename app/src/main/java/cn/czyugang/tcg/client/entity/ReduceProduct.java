package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ReduceProduct {

    /**
     * bookType : string
     * createTime : 2017-12-29T03:36:56.561Z
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
     * statusUpdateTime : 2017-12-29T03:36:56.561Z
     * storeId : string
     * updateTime : 2017-12-29T03:36:56.561Z
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
    public double score;
    @SerializedName("status")
    public String status;
    @SerializedName("statusUpdateTime")
    public String statusUpdateTime;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("updateTime")
    public String updateTime;
}
