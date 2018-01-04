package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.utils.JsonParse;

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
    @SerializedName(value = "id",alternate = {"productStoreId"})
    public String id = "";
    @SerializedName("order")
    public int order = 0;
    @SerializedName("praise")
    public int praise = 0;
    @SerializedName("productId")
    public String productId = "";
    @SerializedName("qrCodeUrl")
    public String qrCodeUrl = "";
    @SerializedName("recycled")
    public String recycled = "";
    @SerializedName("sales")
    public int sales = 0;
    @SerializedName("score")
    public double score = 0;
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
    @SerializedName("webDescribe")
    public String webDescribe = "";

    @SerializedName("picId")
    public String pic = "";
    public boolean hadCollect=false;

    @SerializedName("storeId")
    public String storeId = "";
    public String inventoryId = "";   //库存  inventoryOfId[0].id -->  /api/auth/v1/product/shopping/getAttributes?storeInventoryId -->  GoodsSpecResponse
    public double packagePrice = 0; //包装费  inventoryOfId[0].packagePrice
    public double showPrice = 0;
    public int showRemain = 0;
    transient public GoodsSpecResponse specResponse = null;    //规格
    transient public ArrayList<String> picList = null;

    public boolean isMultiSpec() {
        return skuType.equals("MULTI");
    }

    public boolean isSaleAble() {
        return enableSale.equals("ENABLE");
    }

    public double getShowPrice() {
        return showPrice;
    }

    public String getShowPriceStr() {
        return String.format("￥%.2f", showPrice);
    }

    public int getShowRemain(String spec) {
        return showRemain;
    }

    public String getPackagePriceStr() {
        return String.format("￥%.2f", packagePrice);
    }

    public CharSequence getTag() {
        return "";
    }


    public void mergeInfoFromProductInfo(JSONObject jsonObject) {
        if (jsonObject == null) return;
        appDescribe = jsonObject.optString("appDescribe");
        brandId = jsonObject.optString("brandId");
        classifyId = jsonObject.optString("classifyId");
        enableSale = jsonObject.optString("enableSale");
        skuType = jsonObject.optString("skuType");
        subTitle = jsonObject.optString("subTitle");
        title = jsonObject.optString("title");
        userId = jsonObject.optString("userId");
        webDescribe = jsonObject.optString("webDescribe");
    }

    /*
    *   详情 标签
    * */

    transient public List<Tag> productTagList = null;
    transient public List<Tag> serviceTagList = null;

    public void parseTagList(JSONObject values) {
        productTagList = JsonParse.fromJsonInValue(values, "productTagList", new JsonParse.Type(List.class, Tag.class),new ArrayList<>());
        serviceTagList = JsonParse.fromJsonInValue(values, "serviceTagList", new JsonParse.Type(List.class, Tag.class),new ArrayList<>());

/*        productTagList.add(new Tag("标签1"));
        productTagList.add(new Tag("标签1"));
        productTagList.add(new Tag("标签1"));
        productTagList.add(new Tag("标签1"));
        productTagList.add(new Tag("标签1"));

        serviceTagList.add(new Tag("标签1"));
        serviceTagList.add(new Tag("标签1"));
        serviceTagList.add(new Tag("标签1"));*/
    }

    public static class Tag {
        @SerializedName("name")
        public String name;
        @SerializedName("picId")
        public String picId;

        public Tag() {
        }

        public Tag(String name) {
            this.name = name;
        }
    }


    /*
    *
    *    活动
    *
    *       values.activityType 参与活动类型(NORMAL普通商品,QRCODE二维码活动,REDUCE_PRICE降价拍活动)
    *
    *       values.qrcodeProduct 扫码购信息(限量publishNumber，已抢snatch，购买限制buyLimit)(扫码购返回)
    *
    *
    *       values.reducePriceActivityProduct 降价拍商品信息（最低价（冰点价）minPrice, 拼团有效时间(分钟)time）
    *       values.groupList 团列表
    *       values.currentGroupPrice 当前拼团价
    *       values.reducePrice 每加购一件商品降价
    *       values.enableBuy 可购数量
    *
    * */
    public String activityType="NORMAL";
    public QrcodeProduct qrcodeProduct=null;
    public GrouponProduct grouponProduct=null;
    public double currentGroupPrice=0;
    public double reducePrice=0;
    public double enableBuy=0;
    public void parseActivity(JSONObject values){
        activityType=values.optString("activityType");
        qrcodeProduct=JsonParse.fromJsonInValue(values,"qrcodeProduct",QrcodeProduct.class);
        grouponProduct=JsonParse.fromJsonInValue(values,"reducePriceActivityProduct",GrouponProduct.class);
        currentGroupPrice=values.optDouble("currentGroupPrice");
        reducePrice=values.optDouble("reducePrice");
        enableBuy=values.optDouble("enableBuy");
    }

    public boolean isQrcodeActivity(){
        return activityType.equals("QRCODE");
    }

    public boolean isGrouponActivity(){
        return activityType.equals("REDUCE_PRICE");
    }

    /*
    *   二维码优惠
    * */
    public String rate="";
    public int snatch=0;
    public int buyLimit=-1;
    public float getQrcodeBuyRate(){
        if (rate==null||rate.length()<=1) return 0;
        return Float.valueOf(rate.substring(0,rate.length()-1))/100;
    }



    /*
    *   本地外卖分类信息
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

    public static class QrcodeProduct{
        /**
         rate	string
         已抢百分比(%)

         qrcodeUrl	string
         商品二维码图片链接

         createTime	string($date-time)
         创建时间

         activityId	string
         活动id

         degree	number
         优惠度（优惠方式不同，优惠度的单位不同。当直接打折时，单位为折；当优惠金额时，单位为元）

         productStoreId	string
         店铺商品id

         remainderNumber	integer($int32)
         剩余数量

         way	string
         优惠方式（直接打折：DISCOUNT，优惠金额：MONEY）

         snatch	integer($int32)
         已抢

         buyLimit	integer($int32)
         购买限制（-1表示不限）

         updateTime	string($date-time)
         更新时间

         deleteFlag	string
         删除标志量(NORMAL-未删除,DELETED-已删除)

         publishNumber	integer($int32)
         发行数量（活动库存）

         id	string
         storeId	string
         店铺id
         */

        @SerializedName("activityId")
        public String activityId;
        @SerializedName("buyLimit")
        public int buyLimit; //购买限制（-1表示不限）
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("degree")
        public double degree;
        @SerializedName("deleteFlag")
        public String deleteFlag;
        @SerializedName("id")
        public String id;
        @SerializedName("productStoreId")
        public String productStoreId;
        @SerializedName("publishNumber")
        public int publishNumber;       //发行数量（活动库存）
        @SerializedName("qrcodeUrl")
        public String qrcodeUrl;
        @SerializedName("rate")
        public String rate;
        @SerializedName("remainderNumber")
        public int remainderNumber;     //剩余数量
        @SerializedName("snatch")
        public int snatch;  //已抢
        @SerializedName("storeId")
        public String storeId;
        @SerializedName("updateTime")
        public String updateTime;
        @SerializedName("way")
        public String way;
    }
    public static class GrouponProduct{

        /**
         reduceType	string
         降价规则（固定金额PRICE，按比例RATIO）

         reducePrice	number
         降价量（固定金额时为金额，固定比例时为比例）

         deviceLimit	integer($int32)
         每个设备限购

         currentGroup	integer($int32)
         当前团数

         userId	string
         商品所属商家用户id

         createTime	string($date-time)
         创建时间

         activityId	string
         活动id

         productId	string
         商品库id

         productStoreId	string
         店铺商品id

         time	integer($int32)
         团时长（分钟）

         userLimit	integer($int32)
         每个用户限购

         updateTime	string($date-time)
         更新时间

         minPrice	number
         每件商品最低价

         deleteFlag	string
         删除标志

         id	string
         主键

         groupLimit	integer($int32)
         开团限制（不限时为空）
         */

        @SerializedName("activityId")
        public String activityId;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("currentGroup")
        public int currentGroup;
        @SerializedName("deleteFlag")
        public String deleteFlag;
        @SerializedName("deviceLimit")
        public int deviceLimit;
        @SerializedName("groupLimit")
        public int groupLimit;
        @SerializedName("id")
        public String id;
        @SerializedName("minPrice")
        public int minPrice;
        @SerializedName("productId")
        public String productId;
        @SerializedName("productStoreId")
        public String productStoreId;
        @SerializedName("reducePrice")
        public int reducePrice;
        @SerializedName("reduceType")
        public String reduceType;
        @SerializedName("time")
        public int time;
        @SerializedName("updateTime")
        public String updateTime;
        @SerializedName("userId")
        public String userId;
        @SerializedName("userLimit")
        public int userLimit;
    }
}
