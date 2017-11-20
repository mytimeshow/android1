package cn.czyugang.tcg.client.modules.login.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/23.
 * 找回密码的Contract
 */

public interface InputVerificationCodeContract {
    interface View extends BaseView {
        /**
         * 更新发送验证码倒计时
         *
         * @param time 倒计时
         */
        void updateVerificationCodeCountdown(int time);

        /**
         * 更新手机号码
         *
         * @param mobile 手机号
         */
        void updateMobile(String mobile);
    }

    interface Presenter extends BasePresenter {
        /**
         * 发送验证码
         */
        void sendVerificationCode();

        /**
         * 提交验证码
         *
         * @param code 验证码
         */
        void commitVerificationCode(String code);
    }
}
