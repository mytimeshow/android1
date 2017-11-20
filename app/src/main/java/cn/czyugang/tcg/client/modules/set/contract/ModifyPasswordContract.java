package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/21.
 */

public interface ModifyPasswordContract {
    interface View extends BaseView {
        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 修改密码
         *
         * @param oldPassword
         * @param password
         * @param againPassword
         */
        void modifyPassword(String oldPassword, String password, String againPassword);
    }
}
