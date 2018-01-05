package cn.czyugang.tcg.client.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ruiaa
 * @date 2017/12/18
 */

public class OrderSchedule {
    @SerializedName("arrive")
    public String arrive="";           //配送员/商家已送达
    @SerializedName("arriveServerPoint")
    public String arriveServerPoint="";    //配送员到达服务点
    @SerializedName("businessOrder")
    public String businessOrder="";        //商家接单
    @SerializedName("close")
    public String close="";        //已关闭(取消订单导致关闭,售后被拒绝导致关闭)  取消时间
    @SerializedName("confirmReceipt")
    public String confirmReceipt="";   //买家确认收货
    @SerializedName("create")
    public String create;       //创建订单
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("deleteFlag")
    public String deleteFlag;
    @SerializedName("deliveryTime")
    public int deliveryTime;        //配送时间（接单到送达的时间，分钟）
    @SerializedName("differ")
    public int differ;              //与预计时间相差多少
    @SerializedName("estimateArriveTime")
    public String estimateArriveTime;       //预计到达时间
    @SerializedName("evaluate")
    public String evaluate;     //已评价(用户完成订单)  完成时间
    @SerializedName("id")
    public String id;
    @SerializedName("noUseTicketCode")
    public String noUseTicketCode;  //券码待使用
    @SerializedName("orderId")
    public String orderId;      //关联的基础订单id
    @SerializedName("pay")
    public String pay;      //付款 支付时间
    @SerializedName("pickup")
    public String pickup;       //配送员已取货/已购买
    @SerializedName("receive")
    public String receive;      //配送员已接单
    @SerializedName("settlement")
    public String settlement;       //平台结算时间(不能再申请售后)
    @SerializedName("stockUp")
    public String stockUp;      //商家备货/发货
    @SerializedName("storeId")
    public String storeId;      //店铺id
    @SerializedName("updateTime")
    public String updateTime;
    @SerializedName("useTicketCode")
    public String useTicketCode;        //券码已使用
}
