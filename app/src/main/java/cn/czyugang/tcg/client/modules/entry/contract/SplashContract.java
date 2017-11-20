package cn.czyugang.tcg.client.modules.entry.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/5.
 * 启动页的Contract
 */

public interface SplashContract {
    interface View extends BaseView {
        /**
         * @return 界面是否在前台
         */
        boolean isForeground();

        /**
         * 弹出提示更新对话框
         */
        void showUpdateDialog();

        /**
         * 启动主页面
         */
        void startMainActivity();

        /**
         * 启动新特性页面
         */
        void startNewFeatureActivity();

        /**
         * 启动广告页面
         */
        void startADActivity();

        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 重新进入启动页面
         */
        void onResume();
    }
}
