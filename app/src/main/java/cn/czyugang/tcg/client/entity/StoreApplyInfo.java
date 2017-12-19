package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.Map;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class StoreApplyInfo {

    /**
     * applyTime : 2017-12-19T01:51:06.178Z
     * businessAreaId : string
     * businessCityId : string
     * businessContact : string
     * businessId : string
     * businessName : string
     * businessPhone : string
     * businessProvinceId : string
     * businessType : string
     * createTime : 2017-12-19T01:51:06.178Z
     * deleteFlag : string
     * firstOperatorId : string
     * id : string
     * reason : string
     * secondOperatorId : string
     * status : string
     * storeName : string
     * type : string
     * updateTime : 2017-12-19T01:51:06.178Z
     * userId : string
     */

    @SerializedName("applyTime")
    public String applyTime;
    @SerializedName("businessAreaId")
    public String businessAreaId;
    @SerializedName("businessCityId")
    public String businessCityId;
    @SerializedName("businessContact")
    public String businessContact;
    @SerializedName("businessId")
    public String businessId;
    @SerializedName("businessName")
    public String businessName;
    @SerializedName("businessPhone")
    public String businessPhone;
    @SerializedName("businessProvinceId")
    public String businessProvinceId;
    @SerializedName("businessType")
    public String businessType;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("firstOperatorId")
    public String firstOperatorId;
    @SerializedName("id")
    public String id;
    @SerializedName("reason")
    public String reason;
    @SerializedName("secondOperatorId")
    public String secondOperatorId;
    @SerializedName("status")
    public String status;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("type")
    public String type;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("userId")
    public String userId;


    private JSONObject values;
    private String applyStatus = "";
    private Map<String, String> applyStatusDict;
    public String consumerHotline="";
    public String businessManagerName="";
    public String businessManagerPhone="";
    public String valuesReason="";

    public void parse(JSONObject values) {
        this.values = values;
        if (values == null) return;
        try {
            applyStatus=values.optString("applyStatus");
            applyStatusDict= JsonParse.parseMap(values,"applyStatusDict");

            //consumerHotline（客服电话）
            consumerHotline=values.optString("consumerHotline");

            //phone（业务经理联系电话）
            businessManagerPhone=values.optString("phone");

            //businessManagerName（业务经理名称）
            businessManagerName=values.optString("businessManagerName");

            //reason=(null)
            valuesReason=values.optString("reason");

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }
}
