package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/11
 */

public class GoodsResponse extends Response<List<Good>> {

    public List<GoodCategory> goodCategoryList=new ArrayList<>();

    public void parse(){
        try{
            if (values==null) {
                LogRui.e("parse####  values null");
                return;
            }

            //排序
            Collections.sort(data, new Comparator<Good>() {
                @Override
                public int compare(Good o1, Good o2) {
                    if (o1.order>o2.order) return -1;
                    if (o1.order<o2.order) return 1;
                    return 0;
                }
            });

            //商品模板基础信息 + 图片
            List<Good> goodInfoList =JsonParse.fromJson(values.getString("productInfoList"),new JsonParse.Type(List.class,Good.class));
            JSONArray picArray=values.getJSONArray("picList");
            for(int i=0,size=picArray.length();i<size;i++){
                JSONObject jsonObject=picArray.getJSONObject(i);
                String goodId=jsonObject.getString("productId");
                for(Good good: goodInfoList){
                    if (goodId.equals(good.id)) {
                        good.pic=jsonObject.getString("picId");
                        break;
                    }
                }
            }

            //模板信息 -->  data
            for(Good g:data){
                for (Good info:goodInfoList) {
                    if (g.productId .equals(info.id)){
                        g.appDescribe=info.appDescribe;
                        g.brandId=info.brandId;
                        g.classifyId=info.classifyId;
                        g.enableSale=info.enableSale;
                        g.skuType=info.skuType;
                        g.title=info.title;
                        g.subTitle=info.subTitle;
                        g.userId=info.userId;
                        g.webDescribe=info.webDescribe;

                        g.pic=info.pic;
                    }
                }
            }



            //分类信息
            goodCategoryList= JsonParse.fromJson(values.getString("firstInfoList"),new JsonParse.Type(List.class,GoodCategory.class));
            for(GoodCategory category:goodCategoryList){
                String second=values.optString("secondClassifyOf"+category.id,"");
                if (!second.equals("")) category.secondCategoryList=JsonParse
                        .fromJson(second,new JsonParse.Type(List.class,GoodCategory.class));
            }

            //库存id
            for (Good g:data){
                JSONArray jsonArray=values.optJSONArray("inventoryOf"+g.id);
                if (jsonArray!=null&&jsonArray.length()!=0) {
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    g.inventoryId=jsonObject.optString("id");
                    g.showPrice=jsonObject.optDouble("price");
                    g.showRemain=jsonObject.optInt("currentInventory");
                }
            }

            LogRui.i("parse####",goodCategoryList);
        }catch (JSONException e){
            LogRui.e("parse####",e);
        }
    }
}
