package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2017/12/16.
 */

public class InformResponse extends Response<List<Inform>> {

    /**
     * createTime : 2017-12-19T03:14:43.246Z
     * deleteFlag : string
     * description : string
     * fileId : string
     * id : string
     * link : string
     * name : string
     * orderNumber : 0
     * status : string
     * updateTime : 2017-12-19T03:14:43.246Z
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

    public int columnFollowNum;
    public boolean columnIsFollow;
    public String userName;
    public String userHead;
    public String userSummary;
    public String userIdentity;
    public String userIdentityDes;// 发布者身份（中文）
    public String userFollowNum;
    public String userFansNum;
    public String userCover;
    public boolean userIsFollow;


    public void parse(){

        try{
            if (values==null) {
                LogRui.e("parse####  values null");
                return;
            }

            // 发布人头像
            JSONArray headArray=values.optJSONArray("idToPublisherFileIdDict");
            if (headArray!=null&&headArray.length()!=0) {
                for (int i=0,size=headArray.length();i<size;i++){
                    JSONObject jsonObject=headArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (Inform inform:data){
                        if (inform.id.equals(id)){
                            inform.headUrl=jsonObject.optString("name");

                        }
                    }
                }

            }

            // 发布人
            JSONArray nameArray=values.optJSONArray("idToPublisherNameDict");
            if (nameArray!=null&&nameArray.length()!=0) {
                for (int i=0,size=nameArray.length();i<size;i++){
                    JSONObject jsonObject=nameArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (Inform inform:data){
                        if (inform.id.equals(id)){
                            inform.userName=jsonObject.optString("name");

                        }
                    }
                }

            }
/*
            // 发布人身份
            JSONArray identityArray=values.optJSONArray("mediaIdToMediaTypeDict");
            if (identityArray!=null&&identityArray.length()!=0) {
                for (int i=0,size=identityArray.length();i<size;i++){
                    JSONObject jsonObject=identityArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (Inform inform:data){
                        if (inform.mediaId.equals(id)){
                            //inform.name=jsonObject.getString("name");

                        }
                    }
                }

            }*/

            // 评论数
            JSONArray commitNumArray=values.optJSONArray("idToCommentCountDict");
            if (commitNumArray!=null&&commitNumArray.length()!=0) {
                for (int i=0,size=commitNumArray.length();i<size;i++){
                    JSONObject jsonObject=commitNumArray.getJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (Inform inform:data){
                        if (inform.id.equals(id)){
                            inform.commentNum=jsonObject.optString("name");

                        }
                    }
                }

            }

            // 点赞数
            JSONArray thumbsNumArray=values.optJSONArray("idToLikeCountDict");
            if (thumbsNumArray!=null&&thumbsNumArray.length()!=0) {
                for (int i=0,size=thumbsNumArray.length();i<size;i++){
                    JSONObject jsonObject=thumbsNumArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (Inform inform:data){
                        if (inform.id.equals(id)){
                            inform.thumbNum=jsonObject.optString("name");

                        }
                    }
                }

            }

            // 是否已点赞资讯字典
            JSONArray isThumbsArray=values.optJSONArray("idToIsLikeDict");
            if (isThumbsArray!=null&&isThumbsArray.length()!=0) {
                for (int i=0,size=isThumbsArray.length();i<size;i++){
                    JSONObject jsonObject=isThumbsArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (Inform inform:data){
                        if (inform.id.equals(id)){
                            inform.isThumbs=jsonObject.optString("name").equals("YES");

                        }
                    }
                }

            }

            // 文章所属栏目
            JSONArray toSortNameArray=values.optJSONArray("sortIdToSortNameDict");
            if (toSortNameArray!=null&&toSortNameArray.length()!=0) {
                for (int i=0,size=toSortNameArray.length();i<size;i++){
                    JSONObject jsonObject=toSortNameArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (Inform inform:data){
                        if (inform.sortId.equals(id)){
                            inform.sortName=jsonObject.optString("name");

                        }
                    }
                }

            }

            // 资讯来源字典
            JSONArray sourceTypeArray=values.optJSONArray("sourceTypeDict");
            if (sourceTypeArray!=null&&sourceTypeArray.length()!=0) {
                for (int i=0,size=sourceTypeArray.length();i<size;i++){
                    JSONObject jsonObject=sourceTypeArray.optJSONObject(i);

                }

            }

            // 栏目信息
            JSONObject columnMsg=values.optJSONObject("infoSort");
            // 栏目关注人数
            columnFollowNum=values.optInt("infoSortKeepCount");
            // 是否已关注栏目
            columnIsFollow=values.optString("isKeepInfoSort").equals("YES");
            // 发布者
            userName=values.optString("publisherName");
            // 发布者头像
            userHead=values.optString("publisherFileId");
            // 发布者简介
            userSummary=values.optString("publisherSummary");
            // 发布者身份
            userIdentity=values.optString("publisherType");
            // 发布者身份（中文）
            userIdentityDes=values.optString("publisherTypeDes");
            // 发布者关注数
            userFollowNum=values.optString("followCountNumber");
            // 发布者粉丝数
            userFansNum=values.optString("fansCountNumber");
            //是否关注作者
            userIsFollow=values.optString("").equals("YES");
            // 个人主页封面
            userCover=values.optString("coverFileId");

        }catch (JSONException e){
            LogRui.e("parse####",e);
        }
    }
}
