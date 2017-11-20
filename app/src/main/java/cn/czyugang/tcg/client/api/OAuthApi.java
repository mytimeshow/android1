package cn.czyugang.tcg.client.api;

import android.net.Uri;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import cn.czyugang.tcg.client.common.AppOAuth;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.common.Network;
import cn.czyugang.tcg.client.entity.AccessToken;
import cn.czyugang.tcg.client.entity.AppToken;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserToken;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import retrofit2.HttpException;

/**
 * Created by wuzihong on 2017/9/18.
 * 授权相关接口
 */

public class OAuthApi {
    /**
     * 获取AppToken
     *
     * @return
     */
    public Observable<Response<AppToken>> getAppToken() {
        Map<String, Object> params = new HashMap<>();
        //系统类型
        params.put("deviceType", Config.OS_TYPE);
        //唯一码
        params.put("deviceCode", Build.SERIAL);
        //系统版本
        params.put("deviceOsVersion", Build.VERSION.SDK_INT);
        //手机型号
        params.put("deviceModel", Build.MANUFACTURER + " " + Build.MODEL);
        //应用版本
        params.put("clientVersion", MyApplication.sVersionName);
        //接口版本
        params.put("version", Config.API_VERSION);
        //资源ID
        params.put("resourceId", "902426932073881600");
        return Network.getInstance()
                .post("api/auth/v1/oauth/applyAccess", params)
                .map(s -> JsonParse.fromJson(s, new JsonParse.Type(Response.class, AppToken.class)));
    }

    /**
     * 获取AccessToken
     *
     * @param appId
     * @param appToken
     * @return
     */
    public Observable<Response<AccessToken>> getAccessToken(String appId, String appToken) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> getAuthCodeParams = new HashMap<>();
        //资源ID下的id
        getAuthCodeParams.put("clientId", "tcg_client_902426932071104512");
        //资源ID下的secret
        getAuthCodeParams.put("clientSecret", "766f85534d5483f93e7f9efd07f35d93");
        getAuthCodeParams.put("appId", appId);
        getAuthCodeParams.put("appToken", appToken);
        //重定向url，authCode通过重定向url后面返回
        getAuthCodeParams.put("redirectUrl", Config.getApiUrl() + "authCode/android");
        //获取AuthCode
        Network.getInstance()
                .get("api/auth/v1/oauth/authCode", getAuthCodeParams)
                .subscribe(s -> {
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        params.put("authCode", Uri.parse(((HttpException) throwable).response().toString()).getQueryParameter("authCode"));
                    }
                });
        return Network.getInstance()
                .post("api/auth/v1/oauth/at", params)
                .map(s -> JsonParse.fromJson(s, new JsonParse.Type(Response.class, AccessToken.class)));
    }

    /**
     * 刷新AccessToken
     *
     * @param appId
     * @param appToken
     * @param refreshToken
     * @return
     */
    public Observable<Response<AccessToken>> refreshAccessToken(String appId, String appToken, String refreshToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("appToken", appToken);
        params.put("refreshToken", refreshToken);
        return Network.getInstance()
                .post("api/auth/v1/oauth/rt", params)
                .map(s -> JsonParse.fromJson(s, new JsonParse.Type(Response.class, AccessToken.class)));
    }

    /**
     * 刷新UserToken
     *
     * @param userId
     * @param refreshToken
     * @return
     */
    public Observable<Response<UserToken>> refreshUserToken(String userId, String refreshToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("userRefreshToken", refreshToken);
        return AppOAuth.getInstance()
                .post("api/auth/v1/oauth/urt", params)
                .map(s -> JsonParse.fromJson(s, new JsonParse.Type(Response.class, UserToken.class)));
    }
}
