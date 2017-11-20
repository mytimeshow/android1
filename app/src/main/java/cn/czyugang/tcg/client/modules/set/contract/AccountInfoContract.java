package cn.czyugang.tcg.client.modules.set.contract;

import java.io.File;
import java.util.Date;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.UserInfo;

/**
 * Created by wuzihong on 2017/10/19.
 */

public interface AccountInfoContract {
    interface View extends BaseView {
        void updateUserInfo(UserInfo userInfo);
    }


    interface Presenter extends BasePresenter {
        void uploadAvatar(File file);

        void modifySexToMan();

        void modifySexToWoman();

        void modifyBirthday(Date birthday);

        UserInfo getUserInfo();
    }
}
