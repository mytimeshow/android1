package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2017/12/16.
 */

public class InformFollowResponse extends Response<List<InformFollow>> {

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
                    JSONObject jsonObject=headArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (InformFollow inform:data){
                        if (inform.id.equals(id)){
                            inform.headUrl=jsonObject.getString("name");

                        }
                    }
                }

            }

            // 发布人
            JSONArray nameArray=values.optJSONArray("idToPublisherNameDict");
            if (nameArray!=null&&nameArray.length()!=0) {
                for (int i=0,size=nameArray.length();i<size;i++){
                    JSONObject jsonObject=nameArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (InformFollow inform:data){
                        if (inform.id.equals(id)){
                            inform.name=jsonObject.getString("name");

                        }
                    }
                }

            }

            // 发布人身份
            JSONArray identityArray=values.optJSONArray("mediaIdToMediaTypeDict");
            if (identityArray!=null&&identityArray.length()!=0) {
                for (int i=0,size=identityArray.length();i<size;i++){
                    JSONObject jsonObject=identityArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (InformFollow inform:data){
                        if (inform.mediaId.equals(id)){
                            //inform.name=jsonObject.getString("name");

                        }
                    }
                }

            }

            // 评论数
            JSONArray commitNumArray=values.optJSONArray("idToCommentCountDict");
            if (commitNumArray!=null&&commitNumArray.length()!=0) {
                for (int i=0,size=commitNumArray.length();i<size;i++){
                    JSONObject jsonObject=commitNumArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (InformFollow inform:data){
                        if (inform.id.equals(id)){
                            inform.commentNum=jsonObject.getString("name");

                        }
                    }
                }

            }

            // 点赞数
            JSONArray thumbsNumArray=values.optJSONArray("idToLikeCountDict");
            if (thumbsNumArray!=null&&thumbsNumArray.length()!=0) {
                for (int i=0,size=thumbsNumArray.length();i<size;i++){
                    JSONObject jsonObject=thumbsNumArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (InformFollow inform:data){
                        if (inform.id.equals(id)){
                            inform.thumbNum=jsonObject.getString("name");

                        }
                    }
                }

            }

            // 是否已点赞资讯字典
            JSONArray isThumbsArray=values.optJSONArray("idToIsLikeDict");
            if (isThumbsArray!=null&&isThumbsArray.length()!=0) {
                for (int i=0,size=isThumbsArray.length();i<size;i++){
                    JSONObject jsonObject=isThumbsArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (InformFollow inform:data){
                        if (inform.id.equals(id)){
                            inform.isThumbs=jsonObject.getBoolean("name");

                        }
                    }
                }

            }

            // 文章所属栏目
            JSONArray toSortNameArray=values.optJSONArray("sortIdToSortNameDict");
            if (toSortNameArray!=null&&toSortNameArray.length()!=0) {
                for (int i=0,size=toSortNameArray.length();i<size;i++){
                    JSONObject jsonObject=toSortNameArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    for (InformFollow inform:data){
                        if (inform.sortId.equals(id)){
                            inform.isThumbs=jsonObject.getBoolean("name");

                        }
                    }
                }

            }

            // 资讯来源字典
            JSONObject sourceTypeObject=values.getJSONObject("sourceTypeDict ");



        }catch (JSONException e){

        }
    }
}
