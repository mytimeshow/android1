package cn.czyugang.tcg.client.entity;

/**
 * Created by wuzihong on 2017/9/19.
 * accessTokem、refreshToken是app级别的
 * accessToken的有限期为24小时，当accessToken过期后可以通过refreshToken获取新的accessToken
 * refreshToken的有限期为6个月，当refreshToken过期不能再获取新的accessToken，终端需要重新授权获取新的accessToken、refreshToken
 */

public class AccessToken {
    private String accessToken;
    private String refreshToken;

    public AccessToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
