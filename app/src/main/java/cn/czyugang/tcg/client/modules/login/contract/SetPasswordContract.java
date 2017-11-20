package cn.czyugang.tcg.client.modules.login.contract;

import cn.czyugang.tcg.client.base.BasePresenter;

/**
 * Created by wuzihong on 2017/9/23.
 * 找回密码的Contract
 */

public interface SetPasswordContract {
    interface Presenter extends BasePresenter {
        /**
         * 设置密码
         *
         * @param password      密码
         * @param againPassword 再次输入的密码
         */
        void setPassword(String password, String againPassword);
    }
}
