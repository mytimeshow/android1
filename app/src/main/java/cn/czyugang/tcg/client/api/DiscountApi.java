package cn.czyugang.tcg.client.api;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.DiscountsResponse;
import cn.czyugang.tcg.client.entity.QrcodeActRespose;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/12/23
 */

public class DiscountApi {

    /*
    *   降价拍
    * */
    //api/auth/v1/marketing/reduce/detail [可接入-v3]商家降价拍活动商品详情  //activityProductId  活动商品id(列表data.id)

    //api/auth/v1/marketing/reduce/detailInfo [可接入-v3]降价拍活动商品详情  //activityProductId  活动商品id

    //api/auth/v1/marketing/reduce/group/detail [可接入-v3]获取降价拍团详情  //activityGroupId  活动团id

    //api/auth/v1/marketing/reduce/product [可接入-v3]获取降价拍商品      //分页    status 当前推广CURRENT,历史推广HISTORY      name 商品名称


    /*
    *   扫码购
    * */
    // /api/auth/v3/order/qrcode/activity/products［可接入-v3］查询扫码购优惠活动商品信息列表
    public static Observable<QrcodeActRespose> getQrcodeGoods(String activityId, String storeId, String order, int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityId", activityId);
        if (storeId != null && !storeId.isEmpty()) map.put("storeId", storeId);
        if (accessTime != null && !accessTime.isEmpty()) {
            map.put("accessTime", accessTime);
            map.put("page", page);
        } else {
            map.put("page", 1);
        }
        map.put("size", 20);
        if (order != null) {
            //排序字段（综合排序：COMPREHENSIVE，评价：EVALUATE，价格：PRICE，销量：SALES，距离:DISTANCE）
            switch (order) {
                case "COMPREHENSIVE": {
                    map.put("orderFields", "COMPREHENSIVE");
                    map.put("orderTypes", "DESC");
                    break;
                }
                case "EVALUATE": {
                    map.put("orderFields", "EVALUATE");
                    map.put("orderTypes", "DESC");
                    break;
                }
                case "SALES": {
                    map.put("orderFields", "SALES");
                    map.put("orderTypes", "DESC");
                    break;
                }
                case "DISTANCE": {
                    map.put("orderFields", "DISTANCE");
                    map.put("orderTypes", "DESC");
                    break;
                }
                case "priceASC": {
                    map.put("orderFields", "PRICE");
                    map.put("orderTypes", "ASC");
                    break;
                }
                case "priceDESC": {
                    map.put("orderFields", "PRICE");
                    map.put("orderTypes", "DESC");
                    break;
                }
            }
        }
        return UserOAuth.getInstance()
                .get("api/auth/v3/order/qrcode/activity/products", map)
                .map(s -> (QrcodeActRespose) JsonParse.fromJson(s, QrcodeActRespose.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v3/order/qrcode/activity/pre/confirm  确认订单数据加载
    //api/auth/v3/order/qrcode/activity/place/by/delivery 下单(走配送)
    //api/auth/v3/order/qrcode/activity/place/in/store 下单(到店)"


    /*
    *   优惠券
    * */
    //api/auth/v1/marketing/user/coupon/get/coupon [Doc-v3]领取优惠券  couponId
    public static Observable<Response<Object>> getCoupon(String couponId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("couponId ", couponId);
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/get/coupon", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/pay/platform/coupon/list [Doc-v3]选择平台优惠券（类型：可用，不可用）分页
    public static Observable<DiscountsResponse> payPlatformCoupon(int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/pay/platform/coupon/list", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/pay/store/coupon/list  [Doc-v3]选取商家优惠券（类型：可用，不可用）分页
    public static Observable<DiscountsResponse> payStoreCoupon(int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/pay/store/coupon/list", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/pay/user/coupon/list      [Doc-v3]用户可使用/不可用优惠券列表(付款时候)分页
    public static Observable<DiscountsResponse> payCoupon(int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/pay/user/coupon/list", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/platform/activity/list      [Doc-v3]选择平台活动
    public static Observable<DiscountsResponse> getPlatformActivity(int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/platform/activity/list", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/store/activity/list      [Doc-v3]选取店铺活动
    public static Observable<DiscountsResponse> getStoreActivity(int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/store/activity/list", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/store/coupon/list      [Doc-v3]店铺领券列表分页 storeId
    public static Observable<DiscountsResponse> getStoreCoupon(String storeId, int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId",storeId);
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/store/coupon/list", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/user/coupon/list      [Doc-v3]我的优惠券（类型：未使用，已使用，已过期）分页
    //type 优惠券类型（UNUSED-未使用，USED-已使用，EXPIRED-已过期）
    public static Observable<DiscountsResponse> userCoupon(int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/user/coupon/list", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // api/auth/v1/marketing/user/coupon/voucher/center      [Doc-v3]领券中心列表分页
    //type 优惠券类型（平台代金券-PCASH，平台优惠券-PDISCOUNT，店铺优惠券-SDISCOUNT）
    public static Observable<DiscountsResponse> getCoupon(int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", 20);
        if (accessTime != null) {
            map.put("page", page);
            map.put("accessTime", accessTime);
        } else {
            map.put("page", 1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/user/coupon/voucher/center", map)
                .map(s -> (DiscountsResponse) JsonParse.fromJson(s, DiscountsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
