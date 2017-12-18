package cn.czyugang.tcg.client.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.OrderDetailResponse;
import cn.czyugang.tcg.client.entity.OrderResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/12/16
 */

public class OrderApi {

    //获取所有订单
    public static Observable<OrderResponse> getAllOrder(int page, String accessTime, String status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("size", 10);
        if (accessTime !=null) map.put("accessTime", accessTime);
        if (status != null) map.put("status", status);
        return UserOAuth.getInstance()
                .get("api/auth/v2/order/product/myOrder", map)
                .map(s -> (OrderResponse) JsonParse.fromJson(s, OrderResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/product/userDelete  [可接入-v2]用户删除订单
    public static Observable<Response<Object>> deleteOrder(List<String> orderIdList) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderIdList", orderIdList);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/userDelete", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<Response<Object>> deleteOrder(String orderId) {
        ArrayList<String> list=new ArrayList<>();
        list.add(orderId);
        return deleteOrder(list);
    }


    //api/auth/v2/order/product/userCancel  [可接入-v2]用户取消订单
    public static Observable<Response<Object>> cancelOrder(String orderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/userCancel", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/product/useFetchCode    [可接入-v2]核销自提订单
    public static Observable<Response<Object>> fetchCode(String baseOrderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("baseOrderId", baseOrderId);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/useFetchCode", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/product/setBusinessNote [可接入-v2]设置订单商家备注
    public static Observable<Response<Object>> setBusinessNote(String note,String orderGoodsId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderGoodsId", orderGoodsId);
        map.put("note",note);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/setBusinessNote", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/product/reach   [可接入-v2]商家送达
    public static Observable<Response<Object>> reach(String orderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/reach", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //GET   /api/auth/v2/order/product/orderDetail  [可接入-v2]订单详情
    public static Observable<OrderDetailResponse> getOrderDetail(String orderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderGoodsId", orderId);
        return UserOAuth.getInstance()
                .get("api/auth/v2/order/product/orderDetail", map)
                .map(s -> (OrderDetailResponse) JsonParse.fromJson(s, OrderDetailResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
