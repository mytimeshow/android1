package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class Store {

    public static final int STORE_TYPE_GOODS = 1;
    public static final int STORE_TYPE_FOOD = 2;

    /**
     * avatarId : string
     * businessCircleId : string
     * businessId : string
     * businessStatus : string
     * createTime : 2017-11-22T06:38:14.562Z
     * deleteFlag : string
     * deliverysEnable : string
     * id : string
     * isAdvanceBooking : string
     * isCatering : string
     * isExpress : string
     * isNonBusinessHours : string
     * isPlatformLogistics : string
     * isSince : string
     * isStoreLogistics : string
     * name : string
     * nonBusinessHoursRange : 0
     * notices : string
     * qrCodeUrl : string
     * status : string
     * updateTime : 2017-11-22T06:38:14.562Z
     */

    @SerializedName("avatarId")
    public String avatarId;         //头像ID
    @SerializedName("businessCircleId")
    public String businessCircleId;
    @SerializedName("businessId")
    public String businessId;       //商户ID
    @SerializedName("businessStatus")
    public String businessStatus;   //营业状态（YES-是，NO-否）
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;       //是否删除(DELETED：删除,NORMAL:正常)
    @SerializedName("id")
    public String id;
    @SerializedName("isAdvanceBooking")
    public String isAdvanceBooking;
    @SerializedName("isCatering")
    private String isCatering;       //接口错误，是否是餐饮业（YES-是，NO-否）
    @SerializedName("isNonBusinessHours")
    public String isNonBusinessHours;
    @SerializedName("name")
    public String name;
    @SerializedName("nonBusinessHoursRange")
    public int nonBusinessHoursRange;
    @SerializedName("notices")
    public String notices;      //店铺公告
    @SerializedName("qrCodeUrl")
    public String qrCodeUrl;
    @SerializedName("status")
    public String status;       //状态（NORMAL-正常，FORBIDDEN-禁用）
    @SerializedName("updateTime")
    public String updateTime;

    /*
    *   配送
    * */
    @SerializedName("deliverysEnable")
    public String deliverysEnable;      //已开启的配送方式（自提-S；快递-E；平台配送-PL；商家配送-SL）用“,”隔开
    @SerializedName("isExpress")
    public String isExpress;            //是否支持快递（YES-是，NO-否）
    @SerializedName("isSince")
    public String isSince;              //是否支持自提（YES-是，NO-否）
    @SerializedName("isPlatformLogistics")
    public String isPlatformLogistics;  //是否支持配送（平台配送）（YES-是，NO-否）
    @SerializedName("isStoreLogistics")
    public String isStoreLogistics;     //是否支持配送（商家配送）（YES-是，NO-否）


    public boolean isFoodStore = false;
    public boolean collected = false;
    public List<String> tagList = new ArrayList<>();
    public double score = 0;
    public double aveDeliveryTime = 0;
    public DeliveryInfo logisticsDelivery = new DeliveryInfo();


    public void init(JSONObject jsonObject) {
        if (jsonObject == null) return;
        try {
            //是否已收藏
            collected = jsonObject.optString("isCollect").equals("YES");

            //标签
            JSONArray tags = jsonObject.optJSONArray("storeTagList");
            if (tags != null && tags.length() > 0) {
                for (int i = 0, size = tags.length(); i < size; i++)
                    tagList.add(tags.optJSONObject(i).optString("name"));
            }

            //店铺分类
            JSONArray classify = jsonObject.optJSONArray("classifyOf" + id);
            if (classify != null && classify.length() > 0) {
                isFoodStore = !classify.getJSONObject(0).optString("classify", "").equals("MARKET");
            }

            //配送信息
            aveDeliveryTime = jsonObject.optDouble("deliveryTime");
            JSONObject logisticsDeliveryJ = jsonObject.optJSONObject("logisticsDeliverySetting");
            if (logisticsDeliveryJ != null) {
                logisticsDelivery = JsonParse.fromJson(logisticsDeliveryJ.toString(), DeliveryInfo.class);
            }

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }

    }

    public boolean isFoodStore() {
        return isFoodStore;
    }

    public boolean isOpening() {
        return businessStatus.endsWith("YES");
    }


    public static class DeliveryInfo {
        public double startDeliveryPrice = 0;//起送价
        public double deliveryPrice = 0;   // 配送费
        public String deliveryTime = "";// 配送时间
    }
}
