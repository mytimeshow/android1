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
    @SerializedName("productAttributeRelationList")
    public List<ProductAttributeRelationListBean> productAttributeRelationList;

    public static class ProductAttributeRelationListBean {

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
        public String value;
        @SerializedName("valueTemplateId")
        public String valueTemplateId;
    }









    public List<String> labels=new ArrayList<>();
    transient public String selectLabel="";

    public GoodsSpec() {
        name="kkk";
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");
        labels.add("kkklllf");

        selectLabel=labels.get(0);
    }
}
