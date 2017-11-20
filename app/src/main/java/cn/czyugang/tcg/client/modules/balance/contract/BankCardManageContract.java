package cn.czyugang.tcg.client.modules.balance.contract;

import java.util.List;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.BankCardInfo;

/**
 * Created by wuzihong on 2017/10/25.
 */

public interface BankCardManageContract {
    interface View extends BaseView {
        /**
         * 更新银行卡列表
         *
         * @param bankCardInfoList
         */
        void updateBankCardList(List<BankCardInfo> bankCardInfoList);
    }

    interface Presenter extends BasePresenter {
        /**
         * 回到银行卡管理界面
         */
        void onResume();

        String getIsSetPayPassword();
    }
}
