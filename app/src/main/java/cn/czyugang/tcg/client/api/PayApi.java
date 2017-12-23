package cn.czyugang.tcg.client.api;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.HashMap;
import java.util.Map;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.utils.LogRui;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class PayApi {

    //api/auth/v1/pay/alipay/testNotify [测试]支付回调
    public static Observable<String>  getAlipayInfo(String orderId,double pay){
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderPayBaseId", orderId);
        map.put("price",pay);
        return UserOAuth.getInstance()
                .get("api/auth/v1/pay/alipay/pay", map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //调起支付宝
    public static void openAlipay(Activity activity,String payInfo){
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
                    Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, payResult.memo, Toast.LENGTH_SHORT).show();
                    LogRui.e("openAlipay####"+payResult.result);
                    LogRui.e("openAlipay####"+payResult.resultStatus);
                }
            });
        }).start();
    }

    private static class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

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
