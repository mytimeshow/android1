package cn.czyugang.tcg.client.api;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.OrderResponse;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/12/16
 */

public class OrderApi {
    public static Observable<OrderResponse> getAllOrder(int page, long accessTime, String status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("size", 10);
        if (accessTime > 0) map.put("accessTime", accessTime);
        if (status != null) map.put("status", status);
        return UserOAuth.getInstance()
                .get("api/auth/v2/order/product/myOrder", map)
                .map(s -> (OrderResponse) JsonParse.fromJson(s, OrderResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
