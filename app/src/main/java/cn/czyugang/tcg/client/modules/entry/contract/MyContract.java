package cn.czyugang.tcg.client.modules.entry.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.UserInfo;

/**
 * Created by wuzihong on 2017/9/15.
 * 个人中心的Contract
 */

public interface MyContract {
    interface View extends BaseView {
        /**
         * 更新用户信息
         */
        void updateUserInfo(UserInfo userInfo);

        /**
         * 退出登录、清除用户信息
         */
        void logout();
    }

    interface Presenter extends BasePresenter {
        /**
         * 重新回到个人中心
         */
        void onResume();
    }
}
