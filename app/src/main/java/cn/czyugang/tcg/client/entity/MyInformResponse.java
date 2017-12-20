package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2017/12/14.
 */

public class MyInformResponse extends  Response<List<MyInform>> {



    public List<String> myInformCotentList=new ArrayList<>();
    /**
     * articleCount : 0
     * createTime : 2017-12-16T01:50:37.828Z
     * deleteFlag : string
     * fansCount : 0
     * followCount : 0
     * id : string
     * updateTime : 2017-12-16T01:50:37.828Z
     * userId : string
     * wordCount : 0
     */

    @SerializedName("articleCount")
    public int articleCount;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("fansCount")
    public int fansCount;
    @SerializedName("followCount")
    public int followCount;
    @SerializedName("id")
    public String id;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("userId")
    public String userId;
    @SerializedName("wordCount")
    public int wordCount;

    public String mySummary;
    public String myName;
    public String myType;
    public int myPublishLimit;
    public String myStatus;
    public boolean myIdentity=false;


    public void parse(){

        try {
            if (values==null) {
                LogRui.e("parse####  values null");
                return;
            }

            //资讯标题
            JSONArray titleArray=values.optJSONArray("infoIdToTitleDict");
            if (titleArray!=null&&titleArray.length()!=0) {
                for (int i=0,size=titleArray.length();i<size;i++){
                    JSONObject jsonObject=titleArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                   for (MyInform myInform:data){
                       if (myInform.infoId.equals(id)){
                           //myInform.setCommitContent(jsonObject.getString("name"));
                           myInform.content=jsonObject.optString("name");

                       }
                   }
                }

            }

            //资讯封面图片
            JSONArray infoCoverArray=values.optJSONArray("infoIdToCoverImageDict");
            if (infoCoverArray!=null&&infoCoverArray.length()!=0) {
                for (int i=0,size=infoCoverArray.length();i<size;i++){
                    JSONObject jsonObject=infoCoverArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.infoId.equals(id)){
                            myInform.imgUrl=jsonObject.optString("name");

                        }
                    }
                }

            }

            //资讯封面视频
            JSONArray infoCoverViewArray=values.optJSONArray("infoIdToCoverViewDict");
            if (infoCoverViewArray!=null&&infoCoverViewArray.length()!=0) {
                for (int i=0,size=infoCoverViewArray.length();i<size;i++){
                    JSONObject jsonObject=infoCoverViewArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.infoId.equals(id)){
                            myInform.imgUrl=jsonObject.optString("name");

                        }
                    }
                }

            }

            //资讯评论数
            JSONArray infoCommitNumArray=values.optJSONArray("infoIdToCommentCountDict");
            if (infoCommitNumArray!=null&&infoCommitNumArray.length()!=0) {
                for (int i=0,size=infoCommitNumArray.length();i<size;i++){
                    JSONObject jsonObject=infoCommitNumArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.infoId.equals(id)){
                            myInform.commitNum=jsonObject.optString("name");
                        }
                    }
                }

            }

            //资讯评论内容
            JSONArray infoCommitContentArray=values.optJSONArray("objectIdToCommentContentDict");
            if (infoCommitContentArray!=null&&infoCommitContentArray.length()!=0) {
                for (int i=0,size=infoCommitContentArray.length();i<size;i++){
                    JSONObject jsonObject=infoCommitContentArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitContent=jsonObject.optString("name");
                        }
                    }
                }

            }

            //资讯回复内容
            JSONArray infoReplyCommitArray=values.optJSONArray("objectIdToReplyContentDict");
            if (infoReplyCommitArray!=null&&infoReplyCommitArray.length()!=0) {
                for (int i=0,size=infoReplyCommitArray.length();i<size;i++){
                    JSONObject jsonObject=infoReplyCommitArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.replyContent=jsonObject.optString("name");
                        }
                    }
                }

            }

           //资讯评论者id/回复者id
            JSONArray infoIdToUserIdArray=values.optJSONArray("objectIdToUserIdDict");
            if (infoIdToUserIdArray!=null&&infoIdToUserIdArray.length()!=0) {
                for (int i=0,size=infoIdToUserIdArray.length();i<size;i++){
                    JSONObject jsonObject=infoIdToUserIdArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                           //myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }
            //资讯评论者/回复者
            JSONArray infoIdToUserNameArray=values.optJSONArray("objectIdToUserNameDict");
            if (infoIdToUserNameArray!=null&&infoIdToUserNameArray.length()!=0) {
                for (int i=0,size=infoIdToUserNameArray.length();i<size;i++){
                    JSONObject jsonObject=infoIdToUserNameArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                           myInform.commitName=jsonObject.optString("name");
                        }
                    }
                }

            }

            //资讯评论者头像
            JSONArray infoToUserHeadArray=values.optJSONArray("objectIdToUserFileIdDict");
            if (infoToUserHeadArray!=null&&infoToUserHeadArray.length()!=0) {
                for (int i=0,size=infoToUserHeadArray.length();i<size;i++){
                    JSONObject jsonObject=infoToUserHeadArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitHead=jsonObject.optString("name");
                        }
                    }
                }

            }

            //被回复者id
            JSONArray infoToTargetUserIdArray=values.optJSONArray("objectIdToTargetUserIdDict");
            if (infoToTargetUserIdArray!=null&&infoToTargetUserIdArray.length()!=0) {
                for (int i=0,size=infoToTargetUserIdArray.length();i<size;i++){
                    JSONObject jsonObject=infoToTargetUserIdArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            //myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }

            //被回复者
            JSONArray infoToTargetUserNameArray=values.optJSONArray("objectIdToTargetUserNameDict");
            if (infoToTargetUserNameArray!=null&&infoToTargetUserNameArray.length()!=0) {
                for (int i=0,size=infoToTargetUserNameArray.length();i<size;i++){
                    JSONObject jsonObject=infoToTargetUserNameArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.replyName=jsonObject.optString("name");
                        }
                    }
                }

            }


            //用户资讯统计信息
            //粉丝数量
            JSONObject userDetail=values.optJSONObject("userInfoCount");
            MyInform myInform=new MyInform();
            fansCount=userDetail.optInt("fansCount");
            wordCount=userDetail.optInt("wordCount");
            createTime=userDetail.optString("createTime");
            deleteFlag=userDetail.optString("deleteFlag");
            articleCount=userDetail.optInt("articleCount");
            followCount=userDetail.optInt("followCount");
            myName=UserOAuth.getUserNickname();
            JSONObject userIdentity=values.optJSONObject("mediaBase");
            if(userIdentity!=null){
                myIdentity=true;
                mySummary = userIdentity.optString("summary");
                myName=userIdentity.optString("mediaName");
                myType=userIdentity.optString("type");
                myPublishLimit=userIdentity.optInt("publishLimit");
                myStatus=userIdentity.optString("status");

            }


        }catch (Exception e){
            LogRui.e("parse####",e);
        }
    }

}
