package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.UserInfo;

/**
 * Created by wuzihong on 2017/10/20.
 */

public interface AccountSafeContract {
    interface View extends BaseView {
        /**
         * 更新用户信息
         */
        void updateUserInfo(UserInfo userInfoq);
    }

    interface Presenter extends BasePresenter {
        UserInfo getUserInfo();
    }
}
