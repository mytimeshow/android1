package cn.czyugang.tcg.client.modules.login.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/23.
 * 找回密码的Contract
 */

public interface InputMobileContract {
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
         * @param mobile    手机号
         * @param agreement 是否同意用户协议
         */
        void sendVerificationCode(String mobile, boolean agreement);
    }
}
