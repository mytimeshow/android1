package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class Order {

    @SerializedName("addressId")
    public String addressId;
    @SerializedName("afterSaleStatus")
    public String afterSaleStatus;      // 售后状态（DOING:售后中，FINISH:已售后）
    @SerializedName("bookFlag")
    public String bookFlag;         //预订单标志(UNBOOK-非预订,BOOK-预订)
    @SerializedName("businessDeliveryCost")
    public int businessDeliveryCost;        //平台配送时配送费中商家承担部分
    @SerializedName("businessNote")
    public String businessNote;         //商家备注
    @SerializedName("checked")
    public String checked;      //是否已查看YES/NO
    @SerializedName("closeReason")
    public String closeReason;      //订单关闭原因
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;       //删除标志量(NORMAL-未删除,DELETED-已删除)
    @SerializedName("deliveryCost")
    public int deliveryCost;            //配送费
    @SerializedName("deliveryWay")
    public String deliveryWay;          //配送方式(PLATFORM-平台、STORE-商家、FETCH-自提)
    @SerializedName("expectDeliveryTime")
    public String expectDeliveryTime;       //期望送达时间
    @SerializedName("id")
    public String id = "";
    @SerializedName("isComment")
    public String isComment;        //是否已评价，YES,NO
    @SerializedName("isQrcodeOrderByDelivery")
    public String isQrcodeOrderByDelivery;      //是否扫码购订单(走配送)（YES,NO）
    @SerializedName("isReducePrice")
    public String isReducePrice;        //是否降价拍订单（YES,NO）
    @SerializedName("orderId")
    public String orderId;
    @SerializedName("payTime")
    public String payTime;          //支付时间
    @SerializedName("platformFee")
    public int platformFee;         //平台服务费
    @SerializedName("status")
    public String status;           //订单状态（WAIT_PAY:待付款。PAY:已付款。ORDERS已接单。DELIVERY:已发货。REACH:已送达。FINISH:已完成。
                                        // REFUND:退款中。CLOSE:已关闭）
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("totalReminder")
    public int totalReminder;       //总催单数
    @SerializedName("type")
    public String type;     //类型（商超MARKET、外卖TAKEOUT）
    @SerializedName("updateTime")
    public String updateTime;

    public double totalPayment=0;
    public String storeName="";

    transient public boolean selected = false;
    transient public List<OrderGoods> goodsList = null;
}
