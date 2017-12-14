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
            LogRui.e("parse####  "+data);

            JSONArray jsonArray=values.optJSONArray("infoIdToTitleDict");
            if (jsonArray!=null&&jsonArray.length()!=0) {
                for (int i=0,size=jsonArray.length();i<size;i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                   for (MyInform myInform:data){
                       if (myInform.id.equals(id)){
                           myInform.commitContent=jsonObject.getString("name");
                       }
                   }
                }

            }



        }catch (JSONException e){

        }
    }

}
