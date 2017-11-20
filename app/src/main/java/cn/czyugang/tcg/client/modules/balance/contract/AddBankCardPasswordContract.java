package cn.czyugang.tcg.client.modules.balance.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/26.
 */

public interface AddBankCardPasswordContract {
    interface View extends BaseView {
        /**
         * 调用添加银行卡界面
         */
        void startAddBankCardActivity(String password);

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
         * 验证支付密码
         *
         * @param password
         */
        void verifyPayPassword(String password);

        /**
         * 解绑银行卡
         *
         * @param password
         */
        void unbindBanKCard(String password);
    }
}
