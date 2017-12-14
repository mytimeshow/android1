package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruiaa
 * @date 2017/12/9
 */

public class GoodsSpec {

    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("id")
    public String id;
    @SerializedName("isCustom")
    public String isCustom;
    @SerializedName("name")
    public String name;
    @SerializedName("productId")
    public String productId;
    @SerializedName("templateId")
    public String templateId;
    @SerializedName("uiKey")
    public String uiKey;
    @SerializedName("updateTime")
    public String updateTime;


    public List<GoodsSpecValue> goodsSpecValueList=null;
    public List<String> labelList =new ArrayList<>();
    transient public String selectLabel="";
    transient public boolean isPowerfulToPriceSpec =false;
    transient public boolean isCustomTag=false;

    public void initLabelList(){
        if (goodsSpecValueList==null) return;
        for(GoodsSpecValue goodsSpecValue:goodsSpecValueList){
            labelList.add(goodsSpecValue.value);
        }
        selectLabel=labelList.get(0);
    }


    public String getSelectId(){
        for(GoodsSpecValue specValue:goodsSpecValueList){
            if (specValue.value.equals(selectLabel)) return specValue.id;
        }
        return goodsSpecValueList.get(0).id;
    }

    public static class GoodsSpecValue {

        @SerializedName("baseId")
        public String baseId;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("deleteFlag")
        public String deleteFlag;
        @SerializedName("id")
        public String id;
        @SerializedName("order")
        public String order;
        @SerializedName("picId")
        public String picId;
        @SerializedName("productId")
        public String productId;
        @SerializedName("uiKey")
        public String uiKey;
        @SerializedName("updateTime")
        public String updateTime;
        @SerializedName("value")
        public String value="";
        @SerializedName("valueTemplateId")
        public String valueTemplateId;
    }


}
