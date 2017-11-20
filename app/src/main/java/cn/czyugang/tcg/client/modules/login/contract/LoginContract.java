package cn.czyugang.tcg.client.modules.login.contract;

import android.app.Activity;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/15.
 * 登录的Contract
 */

public interface LoginContract {
    interface View extends BaseView {
        /**
         * 启动绑定手机界面
         */
        void startBindMobileActivity(String type, String authInfo, String authToken);

        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * QQ登录
         *
         * @param activity
         */
        void tencentLogin(Activity activity);

        /**
         * 微信登录
         */
        void wxLogin();

        /**
         * 微博登录
         */
        void weiboLogin(Activity activity);
    }
}
