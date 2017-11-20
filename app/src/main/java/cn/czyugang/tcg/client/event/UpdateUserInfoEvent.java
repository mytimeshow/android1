package cn.czyugang.tcg.client.event;

import cn.czyugang.tcg.client.entity.UserInfo;

/**
 * Created by wuzihong on 2017/10/20.
 * 更新用户信息事件
 */

public class UpdateUserInfoEvent {
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
