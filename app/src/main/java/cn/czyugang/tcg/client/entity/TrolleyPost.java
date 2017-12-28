package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;

/**
 * @author ruiaa
 * @date 2017/12/14
 */

public class TrolleyPost {

    @SerializedName("deleteFlag")
    public String deleteFlag = "NORMAL";       //删除标识,NORMAL正常DELETED删除
    @SerializedName("number")
    public int number;
    @SerializedName("packagePrice")
    public double packagePrice;
    @SerializedName("price")
    public double price;
    @SerializedName("storeInventoryId")
    public String storeInventoryId;
    @SerializedName("tagIdList")
    public List<String> tagIdList = null;

    private TrolleyPost() {
    }

    public static TrolleyPost newTrolleyPost(TrolleyGoods trolleyGoods) {
        TrolleyPost trolleyPost=new TrolleyPost();
        trolleyPost.deleteFlag=trolleyGoods.deleteFlag;
        trolleyPost.number=trolleyGoods.num;
        trolleyPost.packagePrice=trolleyGoods.packagePrice;
        trolleyPost.price=trolleyGoods.price;
        trolleyPost.storeInventoryId=trolleyGoods.storeInventoryId;
        trolleyPost.tagIdList=new ArrayList<String>(Arrays.asList(trolleyGoods.specId.split(",")));
        return trolleyPost;
    }

    public static List<TrolleyPost> newTrolleyPostList(TrolleyStore trolleyStore){
        List<TrolleyPost> postList=new ArrayList<>();
        for(TrolleyGoods trolleyGoods:trolleyStore.trolleyGoodsMap.values()){
            postList.add(newTrolleyPost(trolleyGoods));
        }
        return postList;
    }

    public static List<TrolleyPost> newTrolleyPostList(List<TrolleyStore> trolleyStores){
        List<TrolleyPost> postList=new ArrayList<>();
        for (TrolleyStore trolleyStore:trolleyStores){
            for(TrolleyGoods trolleyGoods:trolleyStore.trolleyGoodsMap.values()){
                postList.add(newTrolleyPost(trolleyGoods));
            }
        }
        return postList;
    }

    public static String newTrolleyPostListJson(TrolleyStore trolleyStore){
        return JsonParse.toJson(newTrolleyPostList(trolleyStore));
    }
}
