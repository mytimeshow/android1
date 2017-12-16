package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/16
 */
public class OrderGoods {

    @SerializedName("brand")
    public String brand;                //品牌名
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;           //删除标志量(NORMAL-未删除,DELETED-已删除)
    @SerializedName("discountPrice")
    public int discountPrice;           //优惠金额
    @SerializedName("id")
    public String id;
    @SerializedName("labels")
    public String labels;               //商品标签数组（以’,’分隔）
    @SerializedName("number")
    public int number;
    @SerializedName("orderGoodsId")
    public String orderGoodsId;         //商品订单id
    @SerializedName("orderPrice")
    public int orderPrice;              //订单金额
    @SerializedName("packagePrice")
    public int packagePrice;
    @SerializedName("payPrice")
    public int payPrice;
    @SerializedName("picId")
    public String picId;
    @SerializedName("productStoreId")
    public String productStoreId;       //商品id
    @SerializedName("productTag")
    public String productTag;
    @SerializedName("qrcodeProductPreferentialId")
    public String qrcodeProductPreferentialId;  //扫码购商品优惠信息id
    @SerializedName("realPrice")
    public int realPrice;               //折购后商品价格
    @SerializedName("serviceTag")
    public String serviceTag;           //服务标签（名称，逗号分隔）
    @SerializedName("specifications")
    public String specifications;       //属性规格数组（以’,’分隔）
    @SerializedName("status")
    public String status;               //‘REFUND’-申请退款中,’RETURN_ALL’-申请退货退款中,’REVOKE’-退款撤销,’FINISH’-已退款,’CLOSE’-已关闭
    @SerializedName("storeInventoryId")
    public String storeInventoryId;
    @SerializedName("subTitle")
    public String subTitle;
    @SerializedName("title")
    public String title;
    @SerializedName("unitPrice")
    public int unitPrice;           //商品原价
    @SerializedName("updateTime")
    public String updateTime;
}
