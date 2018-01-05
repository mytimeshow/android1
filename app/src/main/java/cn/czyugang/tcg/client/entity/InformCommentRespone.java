package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2018/1/5.
 */

public class InformCommentRespone extends Response<List<InformComment>>{

    /**
     * commentId : string
     * content : string
     * createTime : 2018-01-05T06:12:05.688Z
     * deleteFlag : string
     * id : string
     * isAuthor : string
     * likeCount : 0
     * status : string
     * targetUserId : string
     * targetUserName : string
     * userId : string
     * userName : string
     */

    @SerializedName("commentId")
    public String commentId;
    @SerializedName("content")
    public String content;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("id")
    public String id;
    @SerializedName("isAuthor")
    public String isAuthor;
    @SerializedName("likeCount")
    public int likeCount;
    @SerializedName("status")
    public String status;
    @SerializedName("targetUserId")
    public String targetUserId;
    @SerializedName("targetUserName")
    public String targetUserName;
    @SerializedName("userId")
    public String userId;
    @SerializedName("userName")
    public String userName;

    public  void  parse(){
        if (values == null) {
            LogRui.e("parse####  values null");
            return;
        }
        // 回复列表
        JSONArray commentReplyArray=values.optJSONArray("replyListOf");
        if (commentReplyArray!=null&&commentReplyArray.length()!=0) {
            for (int i=0,size=commentReplyArray.length();i<size;i++){
                JSONObject jsonObject=commentReplyArray.optJSONObject(i);
                String id=jsonObject.optString("commentId");
                for (InformComment informComment:data){
                    if (informComment.id.equals(id)){

                    }
                }
            }

        }

        // 是否已点赞资讯字典
        JSONArray isThumbsArray=values.optJSONArray("idToIsLikeCommentDict");
        if (isThumbsArray!=null&&isThumbsArray.length()!=0) {
            for (int i=0,size=isThumbsArray.length();i<size;i++){
                JSONObject jsonObject=isThumbsArray.optJSONObject(i);
                String id=jsonObject.optString("id");
                for (InformComment informComment:data){
                    if (informComment.id.equals(id)){
                        informComment.isThumbs=jsonObject.optString("name").equals("YES");

                    }
                }
            }

        }
    }


}
