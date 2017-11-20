package cn.czyugang.tcg.client.modules.login.contract;

import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/9/23.
 * 找回密码的Contract
 */

public interface SetPasswordSucceedContract {
    interface View extends BaseView {
        /**
         * 更新跳转登录界面倒计时
         *
         * @param time 倒计时
         */
        void updateGotoLoginCountdown(int time);
    }
}
