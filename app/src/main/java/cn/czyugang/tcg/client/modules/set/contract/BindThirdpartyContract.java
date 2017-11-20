package cn.czyugang.tcg.client.modules.set.contract;

import android.app.Activity;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.UserInfo;

/**
 * Created by wuzihong on 2017/10/23.
 */

public interface BindThirdpartyContract {
    interface View extends BaseView {
        /**
         * 更新绑定信息
         *
         * @param userInfo
         */
        void updateBindInfo(UserInfo userInfo);
    }

    interface Presenter extends BasePresenter {
        /**
         * 绑定微信
         */
        void bindWX();

        /**
         * 绑定QQ
         */
        void bindTencent(Activity activity);

        /**
         * 绑定微博
         */
        void bindWeibo(Activity activity);

        /**
         * 解绑微信
         */
        void unbindWX();

        /**
         * 解绑QQ
         */
        void unbindTencent();

        /**
         * 解绑微博
         */
        void unbindWeibo();

        /**
         * 获取用户信息
         *
         * @return
         */
        UserInfo getUserInfo();
    }
}
