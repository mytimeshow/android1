package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.RealNameAuth;

/**
 * Created by wuzihong on 2017/10/21.
 */

public interface SetPayPasswordContract {
    interface View extends BaseView {
        /**
         * 清空密码，重新输入
         */
        void clearPassword();

        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 设置支付密码
         *
         * @param password
         * @param againPassword
         */
        void setPayPassword(String password, String againPassword);

        /**
         * 绑定银行卡
         *
         * @param mPassword
         * @param password
         * @param bankCardInfo
         * @param realNameAuth
         * @param bankMobile
         */
        void bindBankCard(String mPassword, String password, BankCardInfo bankCardInfo, RealNameAuth realNameAuth, String bankMobile);
    }
}
