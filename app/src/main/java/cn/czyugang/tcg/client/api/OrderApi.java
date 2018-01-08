package cn.czyugang.tcg.client.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.AftersaleDetailResponse;
import cn.czyugang.tcg.client.entity.AftersaleRespose;
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
        if (accessTime != null) map.put("accessTime", accessTime);
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
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<Response<Object>> deleteOrder(String orderId) {
        ArrayList<String> list = new ArrayList<>();
        list.add(orderId);
        return deleteOrder(list);
    }


    //api/auth/v2/order/product/userCancel  [可接入-v2]用户取消订单
    public static Observable<Response<Object>> cancelOrder(String orderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/userCancel", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/product/useFetchCode    [可接入-v2]核销自提订单
    public static Observable<Response<Object>> fetchCode(String baseOrderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("baseOrderId", baseOrderId);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/useFetchCode", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/product/setBusinessNote [可接入-v2]设置订单商家备注
    public static Observable<Response<Object>> setBusinessNote(String note, String orderGoodsId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderGoodsId", orderGoodsId);
        map.put("note", note);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/setBusinessNote", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/product/reach   [可接入-v2]商家送达
    public static Observable<Response<Object>> reach(String orderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/reach", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
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


    /*
    *  售后
    * */
    //api/auth/v2/order/after/sale/user/pre [可接入-v2]预加载
    public static Observable<Response<Object>> aftersaleDict() {
        return UserOAuth.getInstance()
                .get("api/auth/v2/order/after/sale/user/pre", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/after/sale/user/return/refund   [可接入-v2]申请退货退款
    public static Observable<Response<Object>> aftersaleRefund(HashMap<String, Object> map) {
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/after/sale/user/return/refund", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/after/sale/user/page    [可接入-v2]获取售后订单 status  售后订单状态(空-查询全部,FINISH-已完结,PROCESSING-处理中)
    public static Observable<AftersaleRespose> aftersaleOrders(String status, String accessTime, int page) {
        HashMap<String, Object> map = new HashMap<>();
        if (status != null) map.put("status", status);
        if (accessTime==null) {
            map.put("page",1);
        }else {
            map.put("accessTime",accessTime);
            map.put("page",page);
        }
        map.put("size",10);
        return UserOAuth.getInstance()
                .get("api/auth/v2/order/after/sale/user/page", map)
                .map(s -> (AftersaleRespose) JsonParse.fromJson(s, AftersaleRespose.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/after/sale/user/get  [可接入-v2]查看售后订单详情(暂缺钱款去向)
    public static Observable<AftersaleDetailResponse> aftersaleOrder(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id );
        return UserOAuth.getInstance()
                .get("api/auth/v2/order/after/sale/user/get", map)
                .map(s -> (AftersaleDetailResponse) JsonParse.fromJson(s,AftersaleDetailResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/after/sale/user/return/revoke   [可接入-v2]撤销退货退款申请
    public static Observable<Response<Object>> aftersaleRevoke(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/after/sale/user/return/revoke", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/after/sale/user/return/logistics    [可接入-v2]录入退货快递信息
    public static Observable<Response<Object>> aftersaleLogistics(String logisticsMode, String logisticsName, String logisticsOrder, String returnSubOrderId) {
        /*
        * logisticsMode	string 物流方式(快递-DELIVERY,其它-OTHER)
        * logisticsName	string 快递公司名
        * logisticsOrder	string  物流单号
        * returnSubOrderId*	string  退货退款申请id
        * */
        HashMap<String, Object> map = new HashMap<>();
        if (logisticsMode!=null&&!logisticsMode.isEmpty())map.put("logisticsMode", logisticsMode);
        if (logisticsName!=null&&!logisticsName.isEmpty())map.put("logisticsName", logisticsName);
        if (logisticsOrder!=null&&!logisticsOrder.isEmpty())map.put("logisticsOrder", logisticsOrder);
        map.put("returnSubOrderId", returnSubOrderId);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/after/sale/user/return/logistics", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v2/order/after/sale/user/return/intervention [可接入-v2]申请平台介入
    public static Observable<Response<Object>> aftersaleIntervention(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/after/sale/user/return/intervention", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
