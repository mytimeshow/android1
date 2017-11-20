package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.UserInfo;

/**
 * Created by wuzihong on 2017/10/19.
 */

public interface SetContract {
    interface View extends BaseView {
        /**
         * 更新用户信息
         */
        void updateUserInfo(UserInfo userInfo);

        /**
         * 用户未登录，隐藏用户信息
         */
        void hideUserInfo();

        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 退出登录
         */
        void logout();
    }
}
