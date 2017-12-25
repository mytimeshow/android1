package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/11/27
 */

public class Record {

    /**
     * createTime : 2017-12-23T03:01:08.760Z
     * deleteFlag : string
     * id : string
     * objectId : string
     * type : string
     * updateTime : 2017-12-23T03:01:08.760Z
     * userId : string
     */

    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("id")
    public String id;
    @SerializedName("objectId")
    public String objectId;
    @SerializedName("type")
    public String type;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("userId")
    public String userId;


    public boolean selected=false;
    public String footmarkTime=null;

    /*
    *  店铺
    * */
    transient public Store store=null;
    public int activityNum=0;
    public double starScore=0;


    /*
    *  商品
    * */



    /*
    *  资讯
    * */
    transient public Inform inform=null;

    public String getInformFrom(){
        if (inform==null) return "";
        String time=inform.publishTime;
        time=time.split(" ")[0];
        return time+"    "+inform.publisherName+"    "+inform.publisherTypeDesc;
    }
}
