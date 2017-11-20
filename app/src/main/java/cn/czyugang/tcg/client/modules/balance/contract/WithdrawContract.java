package cn.czyugang.tcg.client.modules.balance.contract;

import java.util.List;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.BankCardInfo;

/**
 * Created by wuzihong on 2017/11/2.
 */

public interface WithdrawContract {
    interface View extends BaseView {
        /**
         * 更新银行信息
         *
         * @param bankCardInfo
         */
        void updateBankInfo(BankCardInfo bankCardInfo);
    }

    interface Presenter extends BasePresenter {
        void onResume();

        List<BankCardInfo> getBankCardList();

        String getIsSetPayPassword();
    }
}
