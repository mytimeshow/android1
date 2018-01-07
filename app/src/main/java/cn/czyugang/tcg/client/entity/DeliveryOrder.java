package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class DeliveryOrder {

    /**
     * baseOrderId : string
     * baseUserId : string
     * createTime : 2018-01-06T02:12:14.132Z
     * deleteFlag : string
     * deliveryCost : 0
     * deliveryCostBonus : 0
     * distance : 0
     * exUserId : string
     * expectDeliveryTime : string
     * fetchCode : string
     * fetchCodeStatus : string
     * forecastReachTime : 2018-01-06T02:12:14.132Z
     * goodsType : string
     * id : string
     * isAssigned : string
     * isOvertime : string
     * prepareCountdown : 2018-01-06T02:12:14.132Z
     * qrCodeUrl : string
     * receiveAddressId : string
     * status : string
     * storeId : string
     * type : string
     * updateTime : 2018-01-06T02:12:14.132Z
     * userEvaluation : string
     */

    @SerializedName("baseOrderId")
    public String baseOrderId;
    @SerializedName("baseUserId")
    public String baseUserId;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("deliveryCost")
    public int deliveryCost;
    @SerializedName("deliveryCostBonus")
    public int deliveryCostBonus;
    @SerializedName("distance")
    public int distance;
    @SerializedName("exUserId")
    public String exUserId;
    @SerializedName("expectDeliveryTime")
    public String expectDeliveryTime;
    @SerializedName("fetchCode")
    public String fetchCode;
    @SerializedName("fetchCodeStatus")
    public String fetchCodeStatus;
    @SerializedName("forecastReachTime")
    public String forecastReachTime;
    @SerializedName("goodsType")
    public String goodsType;
    @SerializedName("id")
    public String id;
    @SerializedName("isAssigned")
    public String isAssigned;
    @SerializedName("isOvertime")
    public String isOvertime;
    @SerializedName("prepareCountdown")
    public String prepareCountdown;
    @SerializedName("qrCodeUrl")
    public String qrCodeUrl;
    @SerializedName("receiveAddressId")
    public String receiveAddressId;
    @SerializedName("status")
    public String status;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("type")
    public String type;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("userEvaluation")
    public String userEvaluation;
}
