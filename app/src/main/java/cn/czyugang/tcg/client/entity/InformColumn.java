package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/18.
 */

public class InformColumn {
    /**
     * createTime : 2017-12-18T01:25:15.554Z
     * deleteFlag : string
     * description : string
     * fileId : string
     * id : string
     * link : string
     * name : string
     * orderNumber : 0
     * status : string
     * updateTime : 2017-12-18T01:25:15.555Z
     */

    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("description")
    public String description;
    @SerializedName("fileId")
    public String fileId;
    @SerializedName("id")
    public String id;
    @SerializedName("link")
    public String link;
    @SerializedName("name")
    public String name;
    @SerializedName("orderNumber")
    public int orderNumber;
    @SerializedName("status")
    public String status;
    @SerializedName("updateTime")
    public String updateTime;

    public boolean isFollow=false;

    public int followNum;

    public void  InformColumn(){

    }
}
