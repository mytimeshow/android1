package cn.czyugang.tcg.client.entity;

/**
 * Created by wuzihong on 2017/9/18.
 */

public class AppToken {
    private String appId;
    private String appToken;

    public AppToken(String appId, String appToken) {
        this.appId = appId;
        this.appToken = appToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }
}
