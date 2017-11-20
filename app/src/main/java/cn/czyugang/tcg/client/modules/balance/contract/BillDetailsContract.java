package cn.czyugang.tcg.client.modules.balance.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.Bill;

/**
 * Created by wuzihong on 2017/10/28.
 */

public interface BillDetailsContract {
    interface View extends BaseView {
        /**
         * 更新账单
         */
        void updateBill(Bill bill);
    }

    interface Presenter extends BasePresenter {
    }
}
