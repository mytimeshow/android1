package cn.czyugang.tcg.client.wxapi;

import android.os.Bundle;

import cn.czyugang.tcg.client.api.WXApi;
import cn.czyugang.tcg.client.base.BaseActivity;


/**
 * Created by Administrator on 2016/9/3.
 * 微信支付回调界面
 */

public class WXPayEntryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        WXApi.getInstance().handleIntent(getIntent());
    }
}
