package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/18
 */

public class OrderDeliveryLocation {

    /**
     * baseUserId : string
     * createTime : 2017-12-18T04:58:21.474Z
     * deleteFlag : string
     * id : string
     * lat : string
     * lng : string
     * location : string
     * orderId : string
     * updateTime : 2017-12-18T04:58:21.474Z
     */

    @SerializedName("baseUserId")
    public String baseUserId;       //配送员基础用户表id主键
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("id")
    public String id;       //配送轨迹id
    @SerializedName("lat")
    public String lat;      //纬度
    @SerializedName("lng")
    public String lng;      //经度
    @SerializedName("location")
    public String location;     //配送员位置坐标（经纬度，用,隔开）
    @SerializedName("orderId")
    public String orderId;      //基础订单id
    @SerializedName("updateTime")
    public String updateTime;
}
