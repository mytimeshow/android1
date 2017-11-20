package cn.czyugang.tcg.client.modules.set.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/10/20.
 */

public interface ModifyNicknameContract {
    interface View extends BaseView {
        /**
         * 关闭界面
         */
        void finish();
    }

    interface Presenter extends BasePresenter {
        /**
         * 修改昵称
         *
         * @param nickname
         */
        void modifyNickname(String nickname);
    }
}
