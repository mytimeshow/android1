package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2018/1/4.
 */

public class InformDetailResponse extends Response<InformDetail> {

    public String userName;
    public String summary;
    public String publishLimit;
    public String type;
    public int keepCount;
    public int shareCount;
    public int commentCount;
    public int likeCount;
    public int readCount;
    public String sourceType;
    public String userFileId;
    public boolean isFollow;
    public boolean isLike;
    public boolean isKeep;

    public void parse() {
        try {
            if (values == null) {
                LogRui.e("parse####  values null");
                return;
            }
            //用户媒体信息
            JSONObject jsonMediaBase=values.optJSONObject("mediaBase");
            //用户基础信息
            JSONObject jsonUserBase=values.optJSONObject("userBase");
            if (jsonMediaBase==null){
                userName=jsonUserBase.optString("nickname");

            }else {
                userName=jsonMediaBase.optString("mediaName");
                type=jsonMediaBase.optString("type");
                summary=jsonMediaBase.optString("summary");
            }

            //资讯统计信息
            JSONObject jsonInfoCount=values.optJSONObject("infoCount");
            keepCount=jsonInfoCount.optInt("keepCount");
            shareCount=jsonInfoCount.optInt("shareCount");
            commentCount=jsonInfoCount.optInt("commentCount");
            likeCount=jsonInfoCount.optInt("likeCount");
            readCount=jsonInfoCount.optInt("readCount");

         /*   //资讯来源
            JSONArray sourceType=values.optJSONArray("sourceType");
            if (sourceType!=null&&sourceType.length()!=0) {
                for (int i=0,size=sourceType.length();i<size;i++){
                    JSONObject jsonObject=sourceType.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    InformDetail informDetail=new InformDetail();
                    if (informDetail.sourceType.equals(id))
                        informDetail.sourceType=jsonObject.optString("name");
                }

            }*/

            //发布者头像
            userFileId=values.optString("userFileId");
            //是否关注
            isFollow=values.optString("isFollow").equals("YES");
            //是否收藏
            isKeep=values.optString("isKeep").equals("YES");
            //是否点赞
            isLike=values.optString("isLike").equals("YES");

        } catch (Exception e) {
            LogRui.e("parse####",e);
        }
    }

}
