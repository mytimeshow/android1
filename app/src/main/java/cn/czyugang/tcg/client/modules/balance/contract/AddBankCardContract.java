package cn.czyugang.tcg.client.modules.balance.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.RealNameAuth;

/**
 * Created by wuzihong on 2017/10/25.
 */

public interface AddBankCardContract {
    interface View extends BaseView {
        /**
         * 更新资产信息
         */
        void updateBankCardInfo(BankCardInfo bankCardInfo);

        /**
         * 启动银行卡信息验证界面
         */
        void startBankCardVerifyActivity(BankCardInfo bankCardInfo, RealNameAuth realNameAuth);
    }

    interface Presenter extends BasePresenter {
        /**
         * 验证银行卡号
         */
        void verifyBankNumber(String number);

        /**
         * 取消验证请求
         */
        void disposeVerifyBankNumber();

        /**
         * 进入下个界面
         */
        void gotoNext(String number);
    }
}
