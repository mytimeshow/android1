package cn.czyugang.tcg.client.api;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/12/23
 */

public class PromoterApi {

    /*
    *   预加载
    * */
    //api/auth/v3/marketing/promoter/pre
    public static Observable<Response<Object>> promoterInfo() {
        return UserOAuth.getInstance()
                .get("api/auth/v3/marketing/promoter/pre", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v3/marketing/promoter/submit  [可接入-v3]提交申请成为推广员
    public static Observable<Response<Object>> becomePromoter() {
        return UserOAuth.getInstance()
                .post("api/auth/v3/marketing/promoter/submit", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /*
    *   分享
    * */
    //api/auth/v3/marketing/promoter/share/register [可接入-v3]邀请注册分享
    public static Observable<Response<Object>> getRegisterUrl() {
        return UserOAuth.getInstance()
                .get("api/auth/v3/marketing/promoter/share/register", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v3/marketing/promoter/get/code [可接入-v3]获取推广员用户推广码
    public static Observable<Response<Object>> getPromoterCode() {
        return UserOAuth.getInstance()
                .get("api/auth/v3/marketing/promoter/get/code", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v3/marketing/promoter/share/product [可接入-v3]商品推广分享  //店铺商品id
    public static Observable<Response<Object>> getProductUrl(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return UserOAuth.getInstance()
                .get("api/auth/v3/marketing/promoter/share/product", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v3/marketing/promoter/share/record [可接入-v3]记录推广点击数  //店铺商品id,空则记录为注册推广
    public static Observable<Response<Object>> recordShare(String code,String productStoreId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("productStoreId",productStoreId);
        return UserOAuth.getInstance()
                .post("api/auth/v3/marketing/promoter/share/record ", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /*
    *   商品
    * */
    //api/auth/v3/marketing/promoter/page/product [可接入-v3]分页查询推广商品
    public static Observable<Response<Object>> getProductList(String page,String accessTime,String orderType) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",page);
        map.put("size",15);
        map.put("accessTime", accessTime);
        map.put("orderType",orderType);  //排序方式(‘COMPREHENSIVE’-综合排序,’SALES’-销量优先,’RATE’-佣金比率)
        //map.put("name",name);     //商品名称
        return UserOAuth.getInstance()
                .get("api/auth/v3/marketing/promoter/page/product", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    //api/auth/v3/marketing/promoter/get/product [可接入-v3]查看推广商品信息       SpreadProductSetting.id
    public static Observable<Response<Object>> getProduct(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return UserOAuth.getInstance()
                .get("api/auth/v3/marketing/promoter/get/product", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /*
    *   报表
    * */

    //api/auth/v3/marketing/promoter/get/report [可接入-v3]获取报表
    public static Observable<Response<Object>> getReport() {
        return UserOAuth.getInstance()
                .get("api/auth/v3/marketing/promoter/get/report", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    //api/auth/v3/marketing/promoter/page/order [可接入-v3]推广订单分页查询
    public static Observable<Response<Object>> getOrderList(String page,String accessTime,String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",page);
        map.put("size",15);
        map.put("accessTime", accessTime);
        map.put("type",type);  //订单类型(所有订单-空或不传,注册首单-'REGISTER’,商品订单-'PRODUCT’,失效订单-‘INVALID’)
        return UserOAuth.getInstance()
                .get("api/auth/v2/order/product/userDelete", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
