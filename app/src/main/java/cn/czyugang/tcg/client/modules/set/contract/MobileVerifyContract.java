package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/21.
 */

public interface MobileVerifyContract {
    interface View extends BaseView {
        /**
         * 更新发送验证码倒计时
         *
         * @param time 倒计时
         */
        void updateVerificationCodeCountdown(int time);

        /**
         * 启动设置密码界面
         */
        void startSetPasswordActivity();

        /**
         * 启动设置支付密码界面
         */
        void startSetPayPasswordActivity();

        /**
         * 启动找回支付密码界面
         */
        void startForgetPayPasswordActivity();

        /**
         * 启动绑定邮箱界面
         */
        void startBindEmailActivity();

        /**
         * 关闭界面
         */
        void finish();
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
