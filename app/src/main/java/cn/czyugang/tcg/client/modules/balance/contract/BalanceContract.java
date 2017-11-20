package cn.czyugang.tcg.client.modules.balance.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.BalanceInfo;

/**
 * Created by wuzihong on 2017/10/25.
 */

public interface BalanceContract {
    interface View extends BaseView {
        /**
         * 更新资产信息
         */
        void updateBalanceInfo(BalanceInfo balanceInfo);
    }

    interface Presenter extends BasePresenter {
        /**
         * 回到资产页面
         */
        void onResume();

        /**
         * 获取余额信息
         *
         * @return
         */
        BalanceInfo getBalanceInfo();
    }
}
