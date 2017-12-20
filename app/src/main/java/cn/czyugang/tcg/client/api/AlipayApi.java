package cn.czyugang.tcg.client.api;

import android.app.Activity;
import android.text.TextUtils;

import java.util.Map;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class AlipayApi {

    public void pay(Activity activity){
/*        new Thread(() -> {
            PayTask alipay = new PayTask(activity);
            final Map<String, String> result = alipay.payV2(payInfo, true);
            final PayResult payResult = new PayResult(result);
            runOnUiThread(() -> {
                refreshConfigBtn();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(payResult.resultStatus, "9000")) {
                    Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PayActivity.this, payResult.memo, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.setClass(PayActivity.this, OrderDetailActivity.class);
                intent.putExtra("OrderId", orderId);
                startActivity(intent);
                finish();
            });
        }).start();*/
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
