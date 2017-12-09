package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class Good {
    /*
    brandId	string
    品牌ID

    userId	string
    商品所属商家用户id

    createTime	string($date-time)
    创建时间

    enableSale	string
    是否可售（ENABLE:可售，DISABLE:不可售）

    appDescribe	string
    商品描述（目前只是使用该字段）

    sales	integer($int64)
    总销量

    disparage	integer($int64)
    踩数

    recycled	string
    是否被回收（是：YES，否：NO）

    score	number($double)
    平均评分

    status	string
    状态（UNPUBLISH-未发布，WAITCHECK-待审核，CHECKFAILD-审核驳回，WAITSELL-待上架，SELLING-在售，OFFLINE-已下架，FORBIDDEN-禁售，AFORBIDDEN-申请解除禁售）

    updateTime	string($date-time)
    更新时间

    skuType	string
    规格类型（MULTI:多规格,SINGLE:单规格）

    deleteFlag	string
    是否删除(DELETED：删除,NORMAL:正常)

    title	string
    商品标题

    subTitle	string
    商品副标题

    id	string
    主键

    praise	integer($int64)
    赞数

    webDescribe	string
    网页端描述（暂时弃用）

    classifyId	string
    分类id
    */
    @SerializedName("appDescribe")
    public String appDescribe = "";
    @SerializedName("brandId")
    public String brandId = "";
    @SerializedName("classifyId")
    public String classifyId = "";
    @SerializedName("createTime")
    public String createTime = "";
    @SerializedName("deleteFlag")
    public String deleteFlag = "";
    @SerializedName("disparage")
    public int disparage = 0;
    @SerializedName("enableSale")
    public String enableSale = "";//    是否可售（ENABLE:可售，DISABLE:不可售）
    @SerializedName("id")
    public String id = "";
    @SerializedName("praise")
    public int praise = 0;
    @SerializedName("recycled")
    public String recycled = "";
    @SerializedName("sales")
    public int sales = 0;
    @SerializedName("score")
    public int score = 0;
    @SerializedName("skuType")
    public String skuType = ""; //规格类型（MULTI:多规格,SINGLE:单规格）
    @SerializedName("status")
    public String status = "";
    @SerializedName("subTitle")
    public String subTitle = "";
    @SerializedName("title")
    public String title = "";
    @SerializedName("updateTime")
    public String updateTime = "";
    @SerializedName("userId")
    public String userId = "";


    /*
    *   本地分类信息
    * */
    public boolean isCategory = false;
    public String categoryStr = "";
    public int categoryP = -1;
    public int categoryRedDot = 0;


    public static Good createCategory(String categoryStr, int categoryP) {
        Good good = new Good();
        good.isCategory = true;
        good.categoryStr = categoryStr;
        good.categoryP = categoryP;
        return good;
    }

    public boolean isMultiSpec() {
        return !skuType.equals("SINGLE");
    }

    public boolean isSaleAble(){
        return enableSale.equals("ENABLE");
    }

    public List<GoodsSpec> getGoodsSpec(){
        List<GoodsSpec> list=new ArrayList<>();
        list.add(new GoodsSpec());
        list.add(new GoodsSpec());
        list.add(new GoodsSpec());
        list.add(new GoodsSpec());
        list.add(new GoodsSpec());
        return list;
    }
}
