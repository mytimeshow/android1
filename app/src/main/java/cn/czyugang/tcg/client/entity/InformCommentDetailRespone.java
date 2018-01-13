package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2018/1/5.
 */

public class InformCommentDetailRespone extends Response<InformComment>{



    public  void  parse(){
        if (values == null) {
            LogRui.e("parse####  values null");
            return;
        }
        try {
            // 回复列表
            InformComment informComment = data;
            informComment.replyComment = JsonParse.fromJsonInValue(values, "replyListOf" + informComment.id, new JsonParse.Type(List.class, InformComment.class));
            informComment.isThumbs = values.optString("isLikeComment").equals("YES");

        }catch (Exception e){
            LogRui.e("parse####",e);
        }

    }


}
