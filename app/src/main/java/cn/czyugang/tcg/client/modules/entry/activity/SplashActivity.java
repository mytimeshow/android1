package cn.czyugang.tcg.client.modules.entry.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.modules.entry.contract.SplashContract;
import cn.czyugang.tcg.client.modules.entry.presenter.SplashPresenter;

/**
 * Created by wuzihong on 2017/8/29.
 * 启动页
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {
    private boolean mIsForeground = false;//界面是否在前台

    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SplashPresenter(this, context.getSharedPreferences(Config.CONFIG_NAME, Context.MODE_PRIVATE));
        mPresenter.subscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsForeground = true;
        mPresenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsForeground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public boolean isForeground() {
        return mIsForeground;
    }

    @Override
    public void showUpdateDialog() {
        //TODO 显示更新提示框
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void startNewFeatureActivity() {
        startActivity(new Intent(context, NewFeatureActivity.class));
    }

    @Override
    public void startADActivity() {
        //TODO 启动广告页面
    }
}
