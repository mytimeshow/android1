package cn.czyugang.tcg.client.modules.balance.contract;

import java.util.List;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.entity.StaticDict;

/**
 * Created by wuzihong on 2017/10/27.
 */

public interface BillFilterContract {
    interface View extends BaseView {
        /**
         * 更新账单列表
         */
        void updateBillList(List<Bill> billList, boolean isMore);

        /**
         * 更新收入支出
         *
         * @param income
         * @param expense
         */
        void updateIncomeExpense(double income, double expense);
    }

    interface Presenter extends BasePresenter {
        void selectAll();

        void selectIncome();

        void selectExpense();

        void selectType(String type);

        void selectDate(String startTime, String endTime);

        /**
         * 加载更多账单列表
         */
        void loadMoreBillList();

        List<StaticDict> getBillTypeDictList();
    }
}
