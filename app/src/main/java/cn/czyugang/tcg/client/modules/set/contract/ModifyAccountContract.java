package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/20.
 */

public interface ModifyAccountContract {
    interface View extends BaseView {
        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 修改账号名
         *
         * @param account
         */
        void modifyAccount(String account);
    }
}
