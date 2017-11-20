package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/23.
 */

public interface ModifyPayPasswordContract {
    interface View extends BaseView {
        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 修改支付密码
         *
         * @param oldPassword
         * @param password
         * @param againPassword
         */
        void modifyPayPassword(String oldPassword, String password, String againPassword);
    }
}
