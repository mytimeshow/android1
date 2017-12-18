package cn.czyugang.tcg.client.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;

/**
 * @author ruiaa
 * @date 2017/12/18
 */

public class OrderDetailResponse extends Response<Order> {

    /*
    *   基本信息
    * */
    public double totalPayment = 0;// 总支付金额
    public double actualPayment = 0;//实付金额
    public String orderState = "FINISHED";// 订单状态(FINISHED-已完成,CANCELED-已取消)
    public String type = "MARKET"; //订单类型（MARKET-商品商超订单；TAKEOUT-商品外卖订单；PAY-支付订单；WITHDRAW-提现订单;RUNNER-跑腿订单）
    public String orderNo = "";// 订单编号
    public String note = "";// 备注


    public Store store;

    public double totalPrice = 0;               //商品总价
    private double totalPackagePrice = 0;   //总包装费
    public String contactPhone = "";        //商家联系电话
    public Address userAddress = null;      //收货信息(收货地址address,收货人linkman，联系电话phone)
    public OrderDelivery deliveryInfo = null;       //配送订单信息(配送方式type,券码fetchCode)
    public String deliveryUserName = "";        //配送员名称
    public String deliveryUserPhone = "";       //配送员电话
    private Map<String, String> statusMap = new HashMap<>(); //订单状态字典
    public OrderDeliveryLocation deliveryLocation = null;       //配送原当前位置
    public UserBase userBaseInfo = null;        //买家信息(昵称nickName,账号account，手机号phone)
    public double orderPrice = 0;       //订单金额
    public double allDiscountPrice = 0;// 平台扫码购优惠总额
    private Map<String, String> payTypeDict = new HashMap<>();//
    private List<String> payTypeList = new ArrayList<>();

    public OrderSchedule schedule=null;

    public void parse() {
        if (values == null) return;
        try {

            //values.baseOrder 基础订单信息（用户备注note，订单编号orderNo，实付金额actualPayment）
            parseBaseInfo();

            //values.storeInfo 店铺信息
            String storeStr = values.optString("storeInfo");
            if (!CommonUtil.responseIsNull(storeStr)) {
                store = JsonParse.fromJson(storeStr, Store.class);
            } else {
                store = new Store();
            }

            //values.subOrderList 商城订单子订单（商品标题title，规格specifications(逗号分割)，商品图片picId，包装费packagePrice，单价realPrice，数量number）
            String goodsStr = values.optString("subOrderList");
            if (!CommonUtil.responseIsNull(goodsStr)) {
                data.goodsList = JsonParse.fromJson(goodsStr, new JsonParse.Type(List.class, OrderGoods.class));
            } else {
                data.goodsList = new ArrayList<>();
            }

            //values.totalPrice 商品总价
            totalPrice = values.optDouble("totalPrice", 0);

            //values.totalPackagePrice 总包装费
            totalPackagePrice = values.optDouble("totalPackagePrice", 0);

            //values.contactPhone 商家联系电话
            contactPhone = values.optString("contactPhone");

            //values.userAddress 收货信息(收货地址address,收货人linkman，联系电话phone)
            String userAddressStr = values.optString("userAddress");
            if (!CommonUtil.responseIsNull(userAddressStr)) {
                userAddress = JsonParse.fromJson(userAddressStr, Address.class);
            }

            //values.deliveryOrder 配送订单信息(配送方式type,券码fetchCode)
            String deliveryInfoStr = values.optString("deliveryOrder");
            if (!CommonUtil.responseIsNull(deliveryInfoStr)) {
                deliveryInfo = JsonParse.fromJson(deliveryInfoStr, Store.DeliveryInfo.class);
            }

            //values.deliveryUserName 配送员名称
            deliveryUserName = values.optString("deliveryUserName");
            //values.deliveryUserPhone 配送员电话
            deliveryUserPhone = values.optString("deliveryUserPhone");

            //statusDict 订单状态字典
            JSONArray jsonArray = values.getJSONArray("statusDict");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    statusMap.put(jsonObject.getString("id"), jsonObject.getString("name"));
                }
            }

            //values.deliveryLocation 配送原当前位置
            deliveryLocation = JsonParse.fromJsonInValue(values, "deliveryLocation", OrderDeliveryLocation.class);

            //values.userInfo 买家信息(昵称nickName,账号account，手机号phone)
            userBaseInfo = JsonParse.fromJsonInValue(values, "userInfo", UserBase.class);


            //isCommentDict 评价状态字典 "YES" 已评价  "NO"   未评价

            //values.orderPrice 订单金额
            orderPrice = values.optDouble("orderPrice");

            //values.preSale 预售信息，预售订单返回（发货时间：deliveryTimeType+deliveryTime+deliveryTimeUnit组装，是否可退款enableRefund，退款截止时间refundEndTime）
            //preSale_deliveryTimeType 发货时间描述字典，预售订单返回
            // preSale_enableRefund 是否支持退款字典，预售订单返回


            //deliveryWayDict 配送方式字典


            //values.orderSchedule 配送时间点（接单时间receive，到店时间arriveServerPoint，送达时间arrive）
            schedule=JsonParse.fromJsonInValue(values,"orderSchedule",OrderSchedule.class);

            //afterSaleStatusDict 售后状态字典

            //payTypeDict
            payTypeDict = JsonParse.parseMap(values, "payTypeDict");
            //values.payTypeList 支付方式
            payTypeList = JsonParse.fromJsonInValue(values, "payTypeList", new JsonParse.Type(List.class, String.class), payTypeList);

            //values.platformDeliverySetting 平台配送
            //scheduleDict 订单节点字典
            //allDiscountPrice 平台扫码购优惠总额
            //qrcodeProductPreferentialList#id={SubOrderGoodsDetail.qrcodeProductPreferentialId} 扫码购商品优惠信息列表
            //qrcodeProductPreferential_wayDict 优惠方式字典
            //values.deliveryCourierList 配送员信息


        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }

    private void parseBaseInfo() {
        //values.baseOrder 基础订单信息（用户备注note，订单编号orderNo，实付金额actualPayment）
        JSONObject jsonObject = values.optJSONObject("baseOrder");
        if (jsonObject != null) {
            totalPayment = jsonObject.optDouble("totalPayment");
            actualPayment = jsonObject.optDouble("actualPayment");
            orderState = jsonObject.optString("orderState");
            type = jsonObject.optString("type");
            orderNo = jsonObject.optString("orderNo");
            note = jsonObject.optString("note");
        }
    }


    public String getStatusStr(String statusType) {
        if (statusMap.containsKey(statusType)) return statusMap.get(statusType);
        return "";
    }

    public String getTotalAddressStr() {
        if (userAddress == null) return "";
        return userAddress.linkman + "    " + userAddress.phone + "\n" + userAddress.address;
    }

    public String getPayTypeStr() {
        StringBuilder builder=new StringBuilder();
        for(String s:payTypeList){
            if (payTypeDict.containsKey(s)){
                builder.append(payTypeDict.get(s));
            }else {
                builder.append(s);
            }
            builder.append("    ");
        }
        return builder.toString();
    }

}
