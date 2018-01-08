package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.modules.inform.InformDetailsActivity;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2018/1/5.
 */

public class InformCommentRespone extends Response<List<InformComment>>{



    public  void  parse(){
        if (values == null) {
            LogRui.e("parse####  values null");
            return;
        }
        // 回复列表
        for (InformComment informComment:data){
            informComment.replyComment = JsonParse.fromJsonInValue(values,"replyListOf"+informComment.id, new JsonParse.Type(List.class,InformComment.class));
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
