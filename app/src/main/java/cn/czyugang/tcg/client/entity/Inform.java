package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/6.
 */

public class Inform {


    /**
     * city : string
     * content : string
     * coverImageFileId : string
     * coverThumbImageFileId : string
     * coverViewFileId : string
     * createTime : 2017-12-19T03:07:31.128Z
     * deleteFlag : string
     * failReason : string
     * id : string
     * labelIds : string
     * labelNames : string
     * mediaId : string
     * needCheck : string
     * orderNumber : 0
     * orderTime : 2017-12-19T03:07:31.128Z
     * province : string
     * publishTime : 2017-12-19T03:07:31.128Z
     * sortId : string
     * sourceType : string
     * sourceUrl : string
     * status : string
     * title : string
     * updateTime : 2017-12-19T03:07:31.128Z
     * userId : string
     */

    @SerializedName("city")
    public String city;
    @SerializedName("content")
    public String content;
    @SerializedName("coverImageFileId")
    public String coverImageFileId;
    @SerializedName("coverThumbImageFileId")
    public String coverThumbImageFileId;
    @SerializedName("coverViewFileId")
    public String coverViewFileId;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("failReason")
    public String failReason;
    @SerializedName("id")
    public String id;
    @SerializedName("labelIds")
    public String labelIds;
    @SerializedName("labelNames")
    public String labelNames;
    @SerializedName("mediaId")
    public String mediaId;
    @SerializedName("needCheck")
    public String needCheck;
    @SerializedName("orderNumber")
    public int orderNumber;
    @SerializedName("orderTime")
    public String orderTime;
    @SerializedName("province")
    public String province;
    @SerializedName("publisherName")
    public String publisherName;
    @SerializedName("publisherType")
    public String publisherType;
    @SerializedName("publisherTypeDesc")
    public String publisherTypeDesc;
    @SerializedName("publisherUserId")
    public String publisherUserId;
    @SerializedName("publishTime")
    public String publishTime;
    @SerializedName("sortId")
    public String sortId;
    @SerializedName("sourceType")
    public String sourceType;
    @SerializedName("sourceUrl")
    public String sourceUrl;
    @SerializedName("status")
    public String status;
    @SerializedName("title")
    public String title;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("userId")
    public String userId;


    public String headUrl;
    public String imgUrl;
    public String commentNum;
    public String thumbNum;
    public boolean isThumbs;
    public boolean isFollow;
    public String followNum;
    public String userName;
    public String sortName;

    String bannerImg;

    public void InformColumn() {

    }


}
