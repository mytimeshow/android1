package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/23.
 */

public interface BindEmailContract {
    interface View extends BaseView {
        /**
         * 更新发送验证码倒计时
         *
         * @param time 倒计时
         */
        void updateVerificationCodeCountdown(int time);

        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 发送验证码
         *
         * @param email
         */
        void sendVerificationCode(String email);

        /**
         * 绑定邮箱
         *
         * @param email
         * @param code
         */
        void bindEmail(String email, String code);
    }
}
