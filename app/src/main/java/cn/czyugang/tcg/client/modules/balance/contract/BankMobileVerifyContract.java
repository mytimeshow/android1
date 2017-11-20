package cn.czyugang.tcg.client.modules.balance.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/21.
 */

public interface BankMobileVerifyContract {
    interface View extends BaseView {
        /**
         * 更新发送验证码倒计时
         *
         * @param time 倒计时
         */
        void updateVerificationCodeCountdown(int time);

        /**
         * 启动帐号手机验证码界面
         */
        void startMobileVerifyActivity(String mobile);

        /**
         * 启动设置支付密码界面
         */
        void startSetPayPasswordActivity();

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
        void commitVerificationCode(String code, String bank, String bankNumber, String bankType, String name, String idCard);
    }
}
