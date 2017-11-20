package cn.czyugang.tcg.client.modules.entry.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/13.
 * 新特性页的Contract
 */

public interface NewFeatureContract {
    interface View extends BaseView {
        /**
         * 启动主页面
         */
        void startMainActivity();

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
         * 进入下一个界面
         */
        void gotoNext();
    }
}
