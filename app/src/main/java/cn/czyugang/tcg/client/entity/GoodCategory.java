package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class GoodCategory {

    /**
     * createTime : 2017-11-21T10:21:22.129Z
     * deleteFlag : string
     * id : string
     * level : string
     * name : string
     * order : 0
     * parentId : string
     * picId : string
     * remark : string
     * storeId : string
     * updateTime : 2017-11-21T10:21:22.129Z
     */

    @SerializedName("createTime")
    public String createTime;
    @SerializedName("updateTime")
    public String updateTime;

    @SerializedName("deleteFlag")
    public String deleteFlag;

    @SerializedName("storeId")
    public String storeId;
    @SerializedName("id")
    public String id;
    @SerializedName("parentId")
    public String parentId;     //父分类id
    @SerializedName("name")
    public String name;

    @SerializedName("order")
    public int order;       //排序序号

    @SerializedName("level")
    public String level;
    @SerializedName("picId")
    public String picId;
    @SerializedName("remark")
    public String remark;


}
