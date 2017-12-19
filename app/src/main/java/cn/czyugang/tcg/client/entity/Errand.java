package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class Errand {

    /**
     * baseOrderId : string
     * baseUserId : string
     * cancelReason : string
     * createTime : 2017-12-19T12:42:33.434Z
     * deleteFlag : string
     * distance : string
     * exUserId : string
     * getAddressId : string
     * getTime : 2017-12-19T12:42:33.434Z
     * id : string
     * isAssigned : string
     * runType : string
     * sendAddressId : string
     * sendTime : 2017-12-19T12:42:33.434Z
     * status : string
     * updateTime : 2017-12-19T12:42:33.434Z
     */

    @SerializedName("baseOrderId")
    public String baseOrderId;
    @SerializedName("baseUserId")
    public String baseUserId;
    @SerializedName("cancelReason")
    public String cancelReason;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("distance")
    public String distance;
    @SerializedName("exUserId")
    public String exUserId;
    @SerializedName("getAddressId")
    public String getAddressId;
    @SerializedName("getTime")
    public String getTime;
    @SerializedName("id")
    public String id;
    @SerializedName("isAssigned")
    public String isAssigned;
    @SerializedName("runType")
    public String runType;
    @SerializedName("sendAddressId")
    public String sendAddressId;
    @SerializedName("sendTime")
    public String sendTime;
    @SerializedName("status")
    public String status;
    @SerializedName("updateTime")
    public String updateTime;
}
