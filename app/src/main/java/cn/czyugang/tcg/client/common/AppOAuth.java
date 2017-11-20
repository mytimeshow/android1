package cn.czyugang.tcg.client.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.czyugang.tcg.client.api.OAuthApi;
import cn.czyugang.tcg.client.entity.AccessToken;
import cn.czyugang.tcg.client.entity.AppToken;
import cn.czyugang.tcg.client.entity.Progress;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;

/**
 * Created by wuzihong on 2017/9/11.
 * 所有需要认证的接口请求统一入口，进行OAuth管理
 */

public class AppOAuth {
    private static AppOAuth mInstance;
    private Network mNetwork = Network.getInstance();
    private OAuthApi mOAuthApi = new OAuthApi();

    private SharedPreferences mSharedPreferences;
    private AppToken mAppToken;
    private AccessToken mAccessToken;

    private AppOAuth(Context context) {
        mSharedPreferences = context.getSharedPreferences(Config.CONFIG_NAME, Context.MODE_PRIVATE);
        initAppToken();
        initAccessToken();
    }

    /**
     * 初始化，创建实例
     *
     * @param context
     * @return
     */
    public static AppOAuth init(Context context) {
        mInstance = new AppOAuth(context);
        return mInstance;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static AppOAuth getInstance() {
        return mInstance;
    }

    /**
     * GET请求
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<String> get(String url, Map<String, Object> params) {
        return get(url, params, null);
    }

    /**
     * GET请求 带Header
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public Observable<String> get(String url, Map<String, Object> params, Map<String, Object> headers) {
        if (mAppToken != null && mAccessToken != null) {
            return mNetwork.get(url, params, getOAuthHeaders(headers))
                    .flatMap(s -> {
                        //回调统一处理
                        Response response = JsonParse.fromJson(s, Response.class);
                        switch (response.getCode()) {
                            case 14004://AccessToken无效
                                clearAppToken();
                                clearAccessToken();
                                return getAccessToken()
                                        .flatMap(accessToken -> get(url, params, headers));
                            case 14006://AccessToken过期
                                return refreshAccessToken()
                                        .flatMap(accessToken -> get(url, params, headers));
                            default:
                                return Observable.just(s);
                        }
                    });
        }
        return getAccessToken()
                .flatMap(accessToken -> get(url, params, headers));
    }

    /**
     * POST请求
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<String> post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    /**
     * POST请求 带Header
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public Observable<String> post(String url, Map<String, Object> params, Map<String, Object> headers) {
        if (mAppToken != null && mAccessToken != null) {
            return mNetwork.post(url, params, getOAuthHeaders(headers))
                    .flatMap(s -> {
                        //回调统一处理
                        Response response = JsonParse.fromJson(s, Response.class);
                        switch (response.getCode()) {
                            case 14004://AccessToken无效
                                clearAppToken();
                                clearAccessToken();
                                return getAccessToken()
                                        .flatMap(accessToken -> post(url, params, headers));
                            case 14006://AccessToken过期
                                return refreshAccessToken()
                                        .flatMap(accessToken -> post(url, params, headers));
                            default:
                                return Observable.just(s);
                        }
                    });
        }
        return getAccessToken()
                .flatMap(accessToken -> post(url, params, headers));
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<Progress> upload(String url, Map<String, Object> params) {
        return upload(url, params, null);
    }

    /**
     * 上传文件 带Header
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public Observable<Progress> upload(String url, Map<String, Object> params, Map<String, Object> headers) {
        if (mAppToken != null && mAccessToken != null) {
            return mNetwork.upload(url, params, getOAuthHeaders(headers))
                    .flatMap(progress -> {
                        if (TextUtils.isEmpty(progress.getBodyStr())){
                            return Observable.just(progress);
                        }
                        //回调统一处理
                        Response response = JsonParse.fromJson(progress.getBodyStr(), Response.class);
                        switch (response.getCode()) {
                            case 14004://AccessToken无效
                                clearAppToken();
                                clearAccessToken();
                                return getAccessToken()
                                        .flatMap(accessToken -> upload(url, params, headers));
                            case 14006://AccessToken过期
                                return refreshAccessToken()
                                        .flatMap(accessToken -> upload(url, params, headers));
                            default:
                                return Observable.just(progress);
                        }
                    });
        }
        return getAccessToken()
                .flatMap(accessToken -> upload(url, params, headers));
    }

    /**
     * 生成认证接口都要传入的headers
     *
     * @param headers
     * @return
     */
    private Map<String, Object> getOAuthHeaders(Map<String, Object> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        //加入调用接口必传参数
        headers.put("accessToken", mAccessToken.getAccessToken());
        headers.put("appId", mAppToken.getAppId());
        headers.put("appToken", mAppToken.getAppToken());
        //系统类型
        headers.put("deviceType", Config.OS_TYPE);
        //手机型号
        headers.put("deviceModel", Build.MANUFACTURER + " " + Build.MODEL);
        //系统版本
        headers.put("deviceOsVersion", Build.VERSION.SDK_INT);
        //应用版本
        headers.put("clientVersion", MyApplication.sVersionName);
        return headers;
    }

    /**
     * 获取AccessToken
     *
     * @return
     */
    private Observable<AccessToken> getAccessToken() {
        return mOAuthApi.getAppToken()
                .flatMap(response -> {
                    writeAppToken(response.getData());
                    return mOAuthApi.getAccessToken(mAppToken.getAppId(), mAppToken.getAppToken());
                })
                .map(response -> {
                    writeAccessToken(response.getData());
                    return mAccessToken;
                });
    }

    /**
     * 刷新AccessToken
     *
     * @return
     */
    private Observable<AccessToken> refreshAccessToken() {
        return mOAuthApi.refreshAccessToken(mAppToken.getAppId(), mAppToken.getAppToken(), mAccessToken.getRefreshToken())
                .map(response -> {
                    writeAccessToken(response.getData());
                    return mAccessToken;
                });
    }

    /**
     * 初始化AppToken
     */
    private void initAppToken() {
        String appId = mSharedPreferences.getString(Config.CONFIG_KEY_APP_ID, null);
        String appToken = mSharedPreferences.getString(Config.CONFIG_KEY_APP_TOKEN, null);
        if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(appToken)) {
            mAppToken = new AppToken(appId, appToken);
        }
    }

    /**
     * 初始化AccessToken
     */
    private void initAccessToken() {
        String accessToken = mSharedPreferences.getString(Config.CONFIG_KEY_ACCESS_TOKEN, null);
        String refreshToken = mSharedPreferences.getString(Config.CONFIG_KEY_REFRESH_TOKEN, null);
        if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(refreshToken)) {
            mAccessToken = new AccessToken(accessToken, refreshToken);
        }
    }

    /**
     * 持久化AppToken
     *
     * @param appToken
     */
    private void writeAppToken(AppToken appToken) {
        if (appToken == null) {
            return;
        }
        mAppToken = appToken;
        mSharedPreferences.edit()
                .putString(Config.CONFIG_KEY_APP_ID, appToken.getAppId())
                .putString(Config.CONFIG_KEY_APP_TOKEN, appToken.getAppToken())
                .apply();
    }

    /**
     * 清除AppToken
     */
    private void clearAppToken() {
        mAppToken = null;
        mSharedPreferences.edit()
                .remove(Config.CONFIG_KEY_APP_ID)
                .remove(Config.CONFIG_KEY_APP_TOKEN)
                .apply();
    }

    /**
     * 持久化AccessToken
     *
     * @param accessToken
     */
    private void writeAccessToken(AccessToken accessToken) {
        if (accessToken == null) {
            return;
        }
        mAccessToken = accessToken;
        mSharedPreferences.edit()
                .putString(Config.CONFIG_KEY_ACCESS_TOKEN, accessToken.getAccessToken())
                .putString(Config.CONFIG_KEY_REFRESH_TOKEN, accessToken.getRefreshToken())
                .apply();
    }

    /**
     * 清除AccessToken
     */
    private void clearAccessToken() {
        mAccessToken = null;
        mSharedPreferences.edit()
                .remove(Config.CONFIG_KEY_ACCESS_TOKEN)
                .remove(Config.CONFIG_KEY_REFRESH_TOKEN)
                .apply();
    }
}
