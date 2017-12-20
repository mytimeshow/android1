package cn.czyugang.tcg.client.entity;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.modules.common.ImgAdapter;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class SearchStore {

    /**
     * attributeList : ["string"]
     * attributeTemplateList : ["string"]
     * brandId : string
     * classifyList : ["string"]
     * goodRate : 0
     * id : string
     * location : {"lat":0,"lon":0}
     * price : 0
     * productId : string
     * productStoreId : string
     * productType : string
     * saled : 0
     * serviceTagList : ["string"]
     * storeId : string
     * tagList : ["string"]
     * title : string
     * updateTime : 2017-12-20T07:11:42.829Z
     */

    @SerializedName("brandId")
    public String brandId;
    @SerializedName("goodRate")
    public int goodRate;
    @SerializedName("id")
    public String id;
    @SerializedName("price")
    public int price;
    @SerializedName("productId")
    public String productId;
    @SerializedName("productStoreId")
    public String productStoreId;
    @SerializedName("productType")
    public String productType;
    @SerializedName("saled")
    public int saled;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("title")
    public String title;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("attributeList")
    public List<String> attributeList;
    @SerializedName("attributeTemplateList")
    public List<String> attributeTemplateList;
    @SerializedName("classifyList")
    public List<String> classifyList;
    @SerializedName("serviceTagList")
    public List<String> serviceTagList;
    @SerializedName("tagList")
    public List<String> tagList;

    public String pic;
    public String deliveryWay;
    public double startDeliveryPrice;
    public double deliveryPrice;
    public double distance;
    public double deliveryTime;
    public int score = 0;
    public String isNew;
    public String status;

    transient private ImgAdapter imgAdapter = null;
    public List<SearchGood> goodList;
    private List<String> searchResultGoodsImg = null;

    public void bindSearchResultGoodsImg(Activity activity, RecyclerView recyclerView) {
        if (goodList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
        }
        if (imgAdapter == null) {
            searchResultGoodsImg = new ArrayList<>();
            for (SearchGood good : goodList) {
                searchResultGoodsImg.add(good.pic);
            }
            imgAdapter = new ImgAdapter(searchResultGoodsImg, activity);
            imgAdapter.setSizeRes(R.dimen.dp_86);
        }
        recyclerView.setAdapter(imgAdapter);
    }
}
