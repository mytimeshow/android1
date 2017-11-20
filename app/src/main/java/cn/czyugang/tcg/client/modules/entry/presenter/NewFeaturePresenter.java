package cn.czyugang.tcg.client.modules.entry.presenter;

import android.content.SharedPreferences;

import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.modules.entry.contract.NewFeatureContract;

/**
 * Created by wuzihong on 2017/9/13.
 * 新特性页的Presenter
 */

public class NewFeaturePresenter implements NewFeatureContract.Presenter {
    private NewFeatureContract.View mView;

    private SharedPreferences mSharedPreferences;

    public NewFeaturePresenter(NewFeatureContract.View view, SharedPreferences sharedPreferences) {
        mView = view;
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void dispose() {
    }

    /**
     * 进入下一个界面
     */
    @Override
    public void gotoNext() {
        mSharedPreferences.edit()
                .putBoolean(Config.CONFIG_KEY_SHOW_NEW_FEATURE, false)
                .apply();
        if (isShowAD()) {
            mView.startADActivity();
        } else {
            mView.startMainActivity();
        }
        mView.finish();
    }

    /**
     * @return 是否显示广告页面
     */
    private boolean isShowAD() {
        //TODO 判断是否显示广告页面
        return false;
    }
}
