package cn.czyugang.tcg.client.modules.login.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/15.
 * 快捷登录的Contract
 */

public interface ShortcutLoginContract {
    interface View extends BaseView {
        /**
         * 更新发送验证码倒计时
         *
         * @param time 倒计时
         */
        void updateVerificationCodeCountdown(int time);
    }

    interface Presenter extends BasePresenter {
        /**
         * 发送验证码
         *
         * @param mobile 手机号
         */
        void sendVerificationCode(String mobile);

        /**
         * 登录
         *
         * @param mobile 手机号
         * @param code   验证码
         */
        void login(String mobile, String code);
    }
}
