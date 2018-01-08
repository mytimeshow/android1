package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class InformComment {

    /**
     * content : string
     * createTime : 2018-01-05T06:04:22.255Z
     * deleteFlag : string
     * id : string
     * infoId : string
     * isAuthor : string
     * likeCount : 0
     * replyCount : 0
     * status : string
     * userFileId : string
     * userId : string
     * userName : string
     */

    @SerializedName("content")
    public String content;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("id")
    public String id;
    @SerializedName("infoId")
    public String infoId;
    @SerializedName("isAuthor")
    public String isAuthor;
    @SerializedName("likeCount")
    public int likeCount;
    @SerializedName("replyCount")
    public int replyCount;
    @SerializedName("status")
    public String status;
    @SerializedName("userFileId")
    public String userFileId;
    @SerializedName("userId")
    public String userId;
    @SerializedName("userName")
    public String userName;

    public boolean isThumbs;
    public List<InformComment> replyComment=new ArrayList<InformComment>();
}
