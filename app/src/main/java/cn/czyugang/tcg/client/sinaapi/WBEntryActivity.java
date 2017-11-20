package cn.czyugang.tcg.client.sinaapi;

import android.content.Intent;
import android.os.Bundle;

import cn.czyugang.tcg.client.api.WeiboApi;
import cn.czyugang.tcg.client.base.BaseActivity;

/**
 * Created by Administrator on 2016/9/3.
 * 微博回调界面
 */

public class WBEntryActivity extends BaseActivity {
    private WeiboApi mWeiboApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        mWeiboApi = WeiboApi.getInstance();
        mWeiboApi.handleWeiboResponse(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboApi.handleWeiboResponse(intent);
    }
}
