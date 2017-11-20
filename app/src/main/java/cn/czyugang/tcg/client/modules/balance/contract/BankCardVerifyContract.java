package cn.czyugang.tcg.client.modules.balance.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/25.
 */

public interface BankCardVerifyContract {
    interface View extends BaseView {
        /**
         * 启动验证银行手机界面
         *
         * @param mobile
         */
        void startBankMobileVerifyActivity(String mobile);
    }

    interface Presenter extends BasePresenter {
        /**
         * 验证银行卡信息
         *
         * @param bankNumber
         * @param name
         * @param idCard
         * @param mobile
         * @param agreement
         */
        void verifyBankInfo(String bankNumber, String name, String idCard, String mobile, boolean agreement);
    }
}
