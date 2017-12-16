package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2017/12/14.
 */

public class MyInformResponse extends  Response<List<MyInform>> {


    public List<String> myInformCotentList=new ArrayList<>();

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
                    JSONObject jsonObject=titleArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                   for (MyInform myInform:data){
                       if (myInform.infoId.equals(id)){
                           //myInform.setCommitContent(jsonObject.getString("name"));
                           myInform.content=jsonObject.getString("name");

                       }
                   }
                }

            }

            //资讯封面图片
            JSONArray infoCoverArray=values.optJSONArray("infoIdToCoverImageDict");
            if (infoCoverArray!=null&&infoCoverArray.length()!=0) {
                for (int i=0,size=infoCoverArray.length();i<size;i++){
                    JSONObject jsonObject=infoCoverArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.infoId.equals(id)){
                            myInform.imgUrl=jsonObject.getString("name");

                        }
                    }
                }

            }

            //资讯封面视频
            JSONArray infoCoverViewArray=values.optJSONArray("infoIdToCoverViewDict");
            if (infoCoverViewArray!=null&&infoCoverViewArray.length()!=0) {
                for (int i=0,size=infoCoverViewArray.length();i<size;i++){
                    JSONObject jsonObject=infoCoverViewArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.infoId.equals(id)){
                            myInform.imgUrl=jsonObject.getString("name");

                        }
                    }
                }

            }

            //资讯评论数
            JSONArray infoCommitNumArray=values.optJSONArray("infoIdToCommentCountDict");
            if (infoCommitNumArray!=null&&infoCommitNumArray.length()!=0) {
                for (int i=0,size=infoCommitNumArray.length();i<size;i++){
                    JSONObject jsonObject=infoCommitNumArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.infoId.equals(id)){
                            myInform.commitNum=jsonObject.getString("name");
                        }
                    }
                }

            }

            //资讯评论内容
            JSONArray infoCommitContentArray=values.optJSONArray("objectIdToCommentContentDict");
            if (infoCommitContentArray!=null&&infoCommitContentArray.length()!=0) {
                for (int i=0,size=infoCommitContentArray.length();i<size;i++){
                    JSONObject jsonObject=infoCommitContentArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }

            //资讯回复内容
            JSONArray infoReplyCommitArray=values.optJSONArray("objectIdToReplyContentDict");
            if (infoReplyCommitArray!=null&&infoReplyCommitArray.length()!=0) {
                for (int i=0,size=infoReplyCommitArray.length();i<size;i++){
                    JSONObject jsonObject=infoReplyCommitArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }

            //资讯评论者id/回复者id
            JSONArray infoIdToUserIdArray=values.optJSONArray("objectIdToUserIdDict");
            if (infoIdToUserIdArray!=null&&infoIdToUserIdArray.length()!=0) {
                for (int i=0,size=infoIdToUserIdArray.length();i<size;i++){
                    JSONObject jsonObject=infoIdToUserIdArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }

            //资讯评论者头像
            JSONArray infoToUserHeadArray=values.optJSONArray("objectIdToUserFileIdDict");
            if (infoToUserHeadArray!=null&&infoToUserHeadArray.length()!=0) {
                for (int i=0,size=infoToUserHeadArray.length();i<size;i++){
                    JSONObject jsonObject=infoToUserHeadArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }

            //被回复者id
            JSONArray infoToTargetUserIdArray=values.optJSONArray("objectIdToTargetUserIdDict");
            if (infoToTargetUserIdArray!=null&&infoToTargetUserIdArray.length()!=0) {
                for (int i=0,size=infoToTargetUserIdArray.length();i<size;i++){
                    JSONObject jsonObject=infoToTargetUserIdArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }

            //被回复者
            JSONArray infoToTargetUserNameArray=values.optJSONArray("objectIdToTargetUserNameDict");
            if (infoToTargetUserNameArray!=null&&infoToTargetUserNameArray.length()!=0) {
                for (int i=0,size=infoToTargetUserNameArray.length();i<size;i++){
                    JSONObject jsonObject=infoToTargetUserNameArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (MyInform myInform:data){
                        if (myInform.objectId.equals(id)){
                            myInform.commitContent=jsonObject.getString("name");
                        }
                    }
                }

            }

            //用户资讯统计信息
            JSONObject userDetail=values.getJSONObject("userInfoCount");
            userDetail.getString("fansCount");

        }catch (JSONException e){

        }
    }

}
