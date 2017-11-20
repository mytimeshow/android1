package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/21.
 */

public interface SetPasswordContract {
    interface View extends BaseView {
        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 设置密码
         *
         * @param password
         * @param againPassword
         */
        void setPassword(String password, String againPassword);
    }
}
