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
 * @date 2017/12/16
 */

public class OrderResponse extends Response<List<Order>> {

    private Map<String, String> statusMap = new HashMap<>(); //订单状态字典

    public void parse() {
        if (values == null) {
            LogRui.e("parse#### value is null");
            return;
        }
        try {

            //statusDict 订单状态字典
            JSONArray jsonArray = values.getJSONArray("statusDict");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    statusMap.put(jsonObject.getString("id"), jsonObject.getString("name"));
                }
            }

            if (data == null) data = new ArrayList<>();
            if (data.isEmpty()) return;

            //values.subOrderListOf{data.id} 子订单（商品）（商品数量取列表大小，商品图片picId，规格specifications，标签labels，商品名称title，原价unitPrice，折扣价realPrice）
            for (Order order : data) {
                String goodsStr = values.optString("subOrderListOf" + order.id);
                if (!CommonUtil.responseIsNull(goodsStr)) {
                    order.goodsList = JsonParse.fromJson(goodsStr, new JsonParse.Type(List.class, OrderGoods.class));
                } else {
                    order.goodsList = new ArrayList<>();
                }

                //order.goodsList = JsonParse.fromJsonInValue(values, "subOrderListOf" + order.id, new JsonParse.Type(List.class, OrderGoods.class), new ArrayList<OrderGoods>());
            }


            //values.orderIdToTotalPaymentDict#id={data.orderId} 实付款
            HashMap<String, Double> totalPaymentMap = new HashMap<>();
            jsonArray = values.getJSONArray("orderIdToTotalPaymentDict");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    totalPaymentMap.put(jsonObject.getString("id"), jsonObject.getDouble("name"));
                }
                for (Order order : data) {
                    if (totalPaymentMap.containsKey(order.orderId))
                        order.totalPayment = totalPaymentMap.get(order.orderId);
                }
            }

            //values.storeIdToStoreNameDict#id={data.storeId} 店铺名称
            HashMap<String, String> storeNameMap = new HashMap<>();
            jsonArray = values.getJSONArray("storeIdToStoreNameDict");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    storeNameMap.put(jsonObject.getString("id"), jsonObject.getString("name"));
                }
                for (Order order : data) {
                    if (storeNameMap.containsKey(order.storeId))
                        order.storeName = storeNameMap.get(order.storeId);
                }
            }

        } catch (Exception e) {
            LogRui.e("parse####", e);
        }
    }

    public void merge(OrderResponse newOrderResponse) {
        if (newOrderResponse != null) data.addAll(newOrderResponse.data);
    }

    public String getStatusStr(String statusType) {
        if (statusMap.containsKey(statusType)) return statusMap.get(statusType);
        return "";
    }


}
