package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoteOrder {


    /**
     * account : string
     * commission : 0
     * createTime : 2017-12-28T07:27:12.600Z
     * payment : 0
     * picId : string
     * rate : 0
     * settlement : 2017-12-28T07:27:12.600Z
     * status : string
     * storeName : string
     * title : string
     * type : string
     */

    @SerializedName("account")
    public String account;
    @SerializedName("commission")
    public int commission;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("payment")
    public int payment;
    @SerializedName("picId")
    public String picId;
    @SerializedName("rate")
    public int rate;
    @SerializedName("settlement")
    public String settlement;
    @SerializedName("status")
    public String status;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("title")
    public String title;
    @SerializedName("type")
    public String type;

    public String statusDict;
    public String typeDict;
}
