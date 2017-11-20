package cn.czyugang.tcg.client.modules.login.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/15.
 * 帐号登录的Contract
 */

public interface AccountLoginContract {
    interface View extends BaseView {
        /**
         * 显示图形验证码编辑框
         *
         * @param url 图片链接
         */
        void showCodeDialog(String url);

        /**
         * 隐藏图形验证码编辑框
         */
        void dismissCodeDialog();
    }

    interface Presenter extends BasePresenter {
        /**
         * 登录
         *
         * @param account  帐号
         * @param password 密码
         */
        void login(String account, String password);

        /**
         * 带图形验证码登录
         *
         * @param account  帐号
         * @param password 密码
         * @param code     图形验证码
         */
        void login(String account, String password, String code);
    }
}
