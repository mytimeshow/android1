package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/14
 */

public class TrolleyGoodsSync {

    /**
     packagePrice	number
     加入购物车时商品打包费用

     userId	string
     用户id

     productInventoryId	string
     商品总库存id

     productType	string
     商品类型(MARKET:商超,TAKEOUT:外卖)

     price	number
     加入购物车时商品价格

     createTime	string($date-time)
     创建时间

     number	integer($int32)
     购买数量

     productId	string
     对应商品id

     realPrice	number
     折扣后价格

     productStoreId	string
     对应店铺商品id

     uniqueKey	string
     status	string
     购物车状态, NORMAL:正常,INVALID:失效，SOLD:售罄

     isSettle	string
     是否已结算

     updateTime	string($date-time)
     更新时间

     storeInventoryId	string
     所选商品规格(库存)id

     deleteFlag	string
     删除标志

     tagList	string
     标签id列表

     id	string
     主键

     storeId	string
     店铺id
     */









    @SerializedName("tagList")
    public String tagList;
    @SerializedName("uniqueKey")
    public String uniqueKey;
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("userId")
    public String userId;
}
