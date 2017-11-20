package cn.czyugang.tcg.client.modules.entry.presenter;

import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.modules.entry.contract.SplashContract;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by wuzihong on 2017/9/5.
 * 启动页的Presenter
 */

public class SplashPresenter implements SplashContract.Presenter {
    private final int COUNTDOWN = 2;//倒计时时间（单位"秒"）
    private SplashContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private SharedPreferences mSharedPreferences;
    private boolean mCountdownEnd = false;//倒计时是否结束

    public SplashPresenter(SplashContract.View view, SharedPreferences sharedPreferences) {
        mView = view;
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void subscribe() {
        initNewVersion();
        checkUpdates();
        location();
        startCountdown();
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void onResume() {
        if (mCountdownEnd) {
            gotoNext();
        }
    }

    /**
     * 初始化新版本
     */
    private void initNewVersion() {
        int oldVersionCode = mSharedPreferences.getInt(Config.CONFIG_KEY_VERSION_CODE, 0);
        if (oldVersionCode < MyApplication.sVersionCode) {
            mSharedPreferences.edit()
                    .putBoolean(Config.CONFIG_KEY_SHOW_NEW_FEATURE,
                            oldVersionCode < Config.SHOW_NEW_FEATURE_VERSION_CODE)
                    .putInt(Config.CONFIG_KEY_VERSION_CODE, MyApplication.sVersionCode)
                    .putString(Config.CONFIG_KEY_VERSION_NAME, MyApplication.sVersionName)
                    .apply();
        }
    }

    /**
     * 检测更新
     */
    private void checkUpdates() {
        //TODO 检测更新
    }

    /**
     * 定位
     */
    private void location() {
        //TODO 定位
    }

    /**
     * 启动倒计时
     */
    private void startCountdown() {
        mCompositeDisposable.add(Observable.interval(COUNTDOWN, TimeUnit.SECONDS)
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    mCountdownEnd = true;
                    if (mView.isForeground()) {
                        gotoNext();
                    }
                }));
    }

    /**
     * 进入下一个界面
     */
    private void gotoNext() {
        if (isShowNewFeature()) {
            mView.startNewFeatureActivity();
        } else if (isShowAD()) {
            mView.startADActivity();
        } else {
            mView.startMainActivity();
        }
        mView.finish();
    }

    /**
     * @return 是否显示新特性页面
     */
    private boolean isShowNewFeature() {
        return mSharedPreferences.getBoolean(Config.CONFIG_KEY_SHOW_NEW_FEATURE, true);
    }

    /**
     * @return 是否显示广告页面
     */
    private boolean isShowAD() {
        //TODO 判断是否显示广告页面
        return false;
    }
}
