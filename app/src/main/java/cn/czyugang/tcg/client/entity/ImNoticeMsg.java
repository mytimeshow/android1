package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/29.
 */

public class ImNoticeMsg {
    /**
     * createTime : 2018-01-02T01:42:08.370Z
     * deleteFlag : string
     * id : string
     * msgCategoryId : string
     * msgContent : string
     * msgTitle : string
     * msgTitleSub : string
     * notice : string
     * object : string
     * readNumber : 0
     * receiver : string
     * sendAccount : string
     * sendMode : string
     * sendNumber : 0
     * sendTime : 2018-01-02T01:42:08.370Z
     * sendType : string
     * titlePicFileId : string
     * updateTime : 2018-01-02T01:42:08.370Z
     * url : string
     */

    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("id")
    public String id;
    @SerializedName("msgCategoryId")
    public String msgCategoryId;
    @SerializedName("msgContent")
    public String msgContent;
    @SerializedName("msgTitle")
    public String msgTitle;
    @SerializedName("msgTitleSub")
    public String msgTitleSub;
    @SerializedName("notice")
    public String notice;
    @SerializedName("object")
    public String object;
    @SerializedName("readNumber")
    public int readNumber;
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("sendAccount")
    public String send;
    @SerializedName("sendMode")
    public String sendMode;
    @SerializedName("sendNumber")
    public int sendNumber;
    @SerializedName("sendTime")
    public String sendTime;
    @SerializedName("sendType")
    public String sendType;
    @SerializedName("titlePicFileId")
    public String titlePicFileId;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("url")
    public String url;
}
