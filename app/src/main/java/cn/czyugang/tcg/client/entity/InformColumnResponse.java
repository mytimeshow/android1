package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by Administrator on 2017/12/16.
 */

public class InformColumnResponse extends Response<List<InformColumn>> {

    public void parse(){

        try{
            if (values==null) {
                LogRui.e("parse####  values null");
                return;
            }

            //栏目关注人数字典
            JSONArray followCountArray=values.optJSONArray("idToKeepCountDict");
            if (followCountArray!=null&&followCountArray.length()!=0) {
                for (int i=0,size=followCountArray.length();i<size;i++){
                    JSONObject jsonObject=followCountArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (InformColumn inform:data){
                        if (inform.id.equals(id)){
                            inform.followNum=jsonObject.optInt("name");

                        }
                    }
                }

            }
            //是否已栏目关注字典
            JSONArray isFollowArray=values.optJSONArray("idToIsKeepDict");
            if (isFollowArray!=null&&isFollowArray.length()!=0) {
                for (int i=0,size=isFollowArray.length();i<size;i++){
                    JSONObject jsonObject=isFollowArray.optJSONObject(i);
                    String id=jsonObject.optString("id");
                    for (InformColumn inform:data){
                        if (inform.id.equals(id)){
                            inform.isFollow=jsonObject.optString("name").equals("YES")?true:false;
                        }
                    }
                }

            }

        }catch (Exception e){
            LogRui.e("parse####",e);
        }
    }
}
