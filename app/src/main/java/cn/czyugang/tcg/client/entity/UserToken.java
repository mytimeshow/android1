package cn.czyugang.tcg.client.entity;

/**
 * Created by wuzihong on 2017/9/25.
 */

public class UserToken {
    private String userId;
    private String userToken;
    private String userRefreshToken;

    public UserToken(String userId, String userToken, String userRefreshToken) {
        this.userId = userId;
        this.userToken = userToken;
        this.userRefreshToken = userRefreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserRefreshToken() {
        return userRefreshToken;
    }

    public void setUserRefreshToken(String userRefreshToken) {
        this.userRefreshToken = userRefreshToken;
    }
}
