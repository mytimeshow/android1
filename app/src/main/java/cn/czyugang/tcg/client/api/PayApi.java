package cn.czyugang.tcg.client.api;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.order.PayOrderWayActivity;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.string.EncryptUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class PayApi {

    //api/auth/v1/finance/user/prePayOrder [可接入-v2]订单支付预加载
    public static Observable<Response<Object>> prePayOrder(List<String> orderIds) {
        HashMap<String, Object> map = new HashMap<>();
        String str = orderIds.toString();
        str = str.replaceAll(" ","").substring(1, str.length() - 1);
        map.put("orderIds", str);
        return UserOAuth.getInstance()
                .get("api/auth/v1/finance/user/prePayOrder", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<Object>> prePayOrder(String orderId) {
        List<String> orderIds = new ArrayList<>();
        orderIds.add(orderId);
        return prePayOrder(orderIds);
    }

    //api/auth/v1/finance/user/payOrder [DOC-v2]订单支付
    public static Observable<Response<Object>> payOrder(List<String> orderIds, boolean useWallet, String payPassword, String payType) {
        /*
        *  isBalance*	string         是否使用余额抵扣,YES NO
        *  orderIds
        *  payPassword
        *  payTime
        *  payType      支付类型,为空时默认为全部使用余额支付.WECHAT微信支付，ALIPAY:支付宝支付，ICBC工行支付
        * */

        HashMap<String, Object> map = new HashMap<>();
        map.put("orderIds", orderIds);
        map.put("isBalance", useWallet ? "YES" : "NO");
        String time = String.valueOf(System.currentTimeMillis());
        map.put("payPassword", EncryptUtils.password(payPassword, time));
        map.put("payTime", time);
        if (payType != null) map.put("payType", payType);
        return UserOAuth.getInstance()
                .post("api/auth/v1/finance/user/payOrder", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<Object>> payOrder(String orderId, boolean useWallet, String payPassword, String payType) {
        List<String> orderIds = new ArrayList<>();
        orderIds.add(orderId);
        return payOrder(orderIds, useWallet, payPassword, payType);
    }

    //调起支付宝
    public static void openAlipay(Activity activity, String payInfo) {
        new Thread(() -> {
            LogRui.i("openAlipay####1");
            PayTask alipay = new PayTask(activity);
            LogRui.i("openAlipay####2");
            final Map<String, String> result = alipay.payV2(payInfo, true);
            LogRui.i("openAlipay####3");
            final PayResult payResult = new PayResult(result);
            LogRui.i("openAlipay####4");
            activity.runOnUiThread(() -> {
                LogRui.i("openAlipay####5");
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(payResult.resultStatus, "9000")) {
                    if (activity instanceof PayOrderWayActivity){
                        ((PayOrderWayActivity)activity).onPaySuccess();
                    }
                } else {
                    if (activity instanceof PayOrderWayActivity){
                        ((PayOrderWayActivity)activity).onPayFail();
                    }
                    Toast.makeText(activity, payResult.memo, Toast.LENGTH_SHORT).show();
                    LogRui.e("openAlipay####" + payResult.raw);
                    LogRui.e("openAlipay####" + payResult.result);
                    LogRui.e("openAlipay####" + payResult.resultStatus);
                }
            });
        }).start();
    }

    private static class PayResult {
        private String resultStatus = "";
        private String result = "";
        private String memo = "";
        private String raw = "";

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            raw = rawResult.toString();

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }
    }
}
