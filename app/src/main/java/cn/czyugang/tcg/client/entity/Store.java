package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class Store {

    public static final int STORE_TYPE_GOODS=1;
    public static final int STORE_TYPE_FOOD=2;

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
    public String businessId;
    @SerializedName("businessStatus")
    public String businessStatus;   //营业状态（YES-是，NO-否）
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;       //是否删除(DELETED：删除,NORMAL:正常)
    @SerializedName("deliverysEnable")
    public String deliverysEnable;
    @SerializedName("id")
    public String id;
    @SerializedName("isAdvanceBooking")
    public String isAdvanceBooking;
    @SerializedName("isCatering")
    public String isCatering;       //是否是餐饮业（YES-是，NO-否）
    @SerializedName("isExpress")
    public String isExpress;
    @SerializedName("isNonBusinessHours")
    public String isNonBusinessHours;
    @SerializedName("isPlatformLogistics")
    public String isPlatformLogistics;      //是否支持配送（平台配送）（YES-是，NO-否）
    @SerializedName("isSince")
    public String isSince;
    @SerializedName("isStoreLogistics")
    public String isStoreLogistics;
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

    public boolean collected=false;
    public List<String> tagList=new ArrayList<>();
    public double score=0;

    public void init(JSONObject jsonObject){
        if (jsonObject==null) return;
        try{
            collected=jsonObject.optString("isCollect").equals("YES");
            JSONArray tags=jsonObject.optJSONArray("storeTagList");
            if (tags!=null&&tags.length()>0){
                for(int i=0,size=tags.length();i<size;i++)
                    tagList.add(tags.optJSONObject(i).optString("name"));
            }

        }catch (Exception e){
            LogRui.e("init####",e);
        }

    }

    public boolean isFoodStore(){
        return isCatering.equals("YES");
    }

    public boolean isOpening(){
        return businessStatus.endsWith("YES");
    }
}
