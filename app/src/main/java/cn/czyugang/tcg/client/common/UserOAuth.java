package cn.czyugang.tcg.client.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.czyugang.tcg.client.api.OAuthApi;
import cn.czyugang.tcg.client.entity.Progress;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.entity.UserToken;
import cn.czyugang.tcg.client.modules.login.activity.LoginActivity;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.rxbus.LoginEvent;
import cn.czyugang.tcg.client.utils.rxbus.LogoutEvent;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import cn.czyugang.tcg.client.utils.rxbus.UpdateUserInfoEvent;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by wuzihong on 2017/9/25.
 * 用户权限管理
 */

public class UserOAuth {
    private static UserOAuth mInstance;
    private AppOAuth mAppOAuth = AppOAuth.getInstance();
    private OAuthApi mOAuthApi = new OAuthApi();
    private Lock mLock = new ReentrantLock();

    private SharedPreferences mSharedPreferences;
    private UserToken mUserToken;
    private UserInfo mUserInfo;
    private boolean isLogin = false;
    private LoginEvent mLoginEvent;
    private LogoutEvent mLogoutEvent;
    private UpdateUserInfoEvent mUpdateUserInfoEvent;

    private UserOAuth(Context context) {
        mSharedPreferences = context.getSharedPreferences(Config.CONFIG_NAME, Context.MODE_PRIVATE);
        initUserToken();
        initUserInfo();
    }

    /**
     * 初始化，创建实例
     *
     * @param context
     * @return
     */
    public static UserOAuth init(Context context) {
        mInstance = new UserOAuth(context);
        return mInstance;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static UserOAuth getInstance() {
        return mInstance;
    }

    /*
    *   判断是否已经登录，未登录将跳转至登录界面
    *
    * */
    public static boolean judgeOrLogin() {
        if (mInstance.isLogin()) {
            return true;
        } else {
            MyApplication.getContext().startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
            return false;
        }
    }

    public static boolean judgeHadLogin() {
        return mInstance.isLogin;
    }

    /**
     * GET请求
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<String> get(String url, Map<String, Object> params) {
        if (mUserToken != null) {
            return mAppOAuth.get(url, params, getOAuthHeaders(null))
                    .flatMap(s -> {
                        //回调统一处理
                        Response response = JsonParse.fromJson(s, Response.class);
                        switch (response.getCode()) {
                            case 14012://userToken无效
                                logout();
                                throw new LoginExpiredException();
                            case 14013://userToken过期
                                mUserToken.setUserToken(null);
                                return refreshUserToken()
                                        .flatMap(userToken -> get(url, params));
                            default:
                                return Observable.just(s);
                        }
                    });
        }
        logout();
        Subject<String> subject = PublishSubject.<String>create().toSerialized();
        subject.onError(new LoginExpiredException());
        return subject;
    }

    /**
     * POST请求
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<String> post(String url, Map<String, Object> params) {
        if (mUserToken != null) {
            return mAppOAuth.post(url, params, getOAuthHeaders(null))
                    .flatMap(s -> {
                        //回调统一处理
                        Response response = JsonParse.fromJson(s, Response.class);
                        switch (response.getCode()) {
                            case 14012://userToken无效
                                logout();
                                throw new LoginExpiredException();
                            case 14013://userToken过期
                                mUserToken.setUserToken(null);
                                return refreshUserToken()
                                        .flatMap(userToken -> post(url, params));
                            default:
                                return Observable.just(s);
                        }
                    });
        }
        logout();
        Subject<String> subject = PublishSubject.<String>create().toSerialized();
        subject.onError(new LoginExpiredException());
        return subject;
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<Progress> upload(String url, Map<String, Object> params) {
        if (mUserToken != null) {
            return mAppOAuth.upload(url, params, getOAuthHeaders(null))
                    .flatMap(progress -> {
                        if (TextUtils.isEmpty(progress.getBodyStr())) {
                            return Observable.just(progress);
                        }
                        //回调统一处理
                        Response response = JsonParse.fromJson(progress.getBodyStr(), Response.class);
                        switch (response.getCode()) {
                            case 14012://userToken无效
                                logout();
                                throw new LoginExpiredException();
                            case 14013://userToken过期
                                mUserToken.setUserToken(null);
                                return refreshUserToken()
                                        .flatMap(userToken -> upload(url, params));
                            default:
                                return Observable.just(progress);
                        }
                    });
        }
        logout();
        Subject<Progress> subject = PublishSubject.<Progress>create().toSerialized();
        subject.onError(new LoginExpiredException());
        return subject;
    }

    /**
     * 登录
     *
     * @param userToken
     * @param userInfo
     */
    public void login(UserToken userToken, UserInfo userInfo) {
        if (userToken == null || userInfo == null) {
            return;
        }
        writeUserToken(userToken);
        writeUserInfo(userInfo);
        if (!isLogin) {
            isLogin = true;
            if (mLoginEvent == null) {
                mLoginEvent = new LoginEvent();
            }
            RxBus.post(mLoginEvent);
        }
    }

    /**
     * 注销
     */
    public void logout() {
        clearUserToken();
        clearUserInfo();
        if (isLogin) {
            isLogin = false;
            if (mLogoutEvent == null) {
                mLogoutEvent = new LogoutEvent();
            }
            RxBus.post(mLogoutEvent);
        }
    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * 持久化用户信息
     *
     * @param userInfo
     */
    public void writeUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        mUserInfo = userInfo;
        mSharedPreferences.edit()
                .putString(Config.CONFIG_KEY_USER_INFO, JsonParse.toJson(userInfo))
                .apply();
        if (mUpdateUserInfoEvent == null) {
            mUpdateUserInfoEvent = new UpdateUserInfoEvent();
        }
        mUpdateUserInfoEvent.setUserInfo(userInfo);
        RxBus.post(mUpdateUserInfoEvent);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return mUserInfo;
    }


    public static String getUserId() {
        UserInfo userInfo = getInstance().getUserInfo();
        if (userInfo == null) return "";
        UserBase userBase = userInfo.getUserBase();
        if (userBase == null) return "";
        return userBase.getId();
    }


    public static String getUserNickname() {
        UserInfo userInfo = getInstance().getUserInfo();
        if (userInfo == null) return "";
        UserBase userBase = userInfo.getUserBase();
        if (userBase == null) return "";
        return userBase.getNickname();
    }

    public static String getUserPhotoId() {
        UserInfo userInfo = getInstance().getUserInfo();
        if (userInfo == null) return "";
        UserDetail userDetail = userInfo.getUserDetail();
        if (userDetail == null) return "";
        return userDetail.getFileId();
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
        headers.put("userId", mUserToken.getUserId());
        headers.put("userToken", mUserToken.getUserToken());
        return headers;
    }

    /**
     * 刷新UserToken
     *
     * @return
     */
    private Observable<UserToken> refreshUserToken() {
        mLock.lock();
        if (TextUtils.isEmpty(mUserToken.getUserToken())) {
            return mOAuthApi.refreshUserToken(mUserToken.getUserId(), mUserToken.getUserRefreshToken())
                    .flatMap(response -> {
                        switch (response.getCode()) {
                            case 14014://RefreshToken过期
                            case 14015://RefreshToken无效
                                logout();
                                throw new LoginExpiredException();
                            default:
                                writeUserToken(response.getData());
                                mLock.unlock();
                                return Observable.just(mUserToken);
                        }
                    });
        }
        mLock.unlock();
        return Observable.just(mUserToken);
    }

    /**
     * 初始化UserToken
     */
    private void initUserToken() {
        String userId = mSharedPreferences.getString(Config.CONFIG_KEY_USER_ID, null);
        String userToken = mSharedPreferences.getString(Config.CONFIG_KEY_USER_TOKEN, null);
        String userRefreshToken = mSharedPreferences.getString(Config.CONFIG_KEY_USER_REFRESH_TOKEN, null);
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userToken) && !TextUtils.isEmpty(userRefreshToken)) {
            mUserToken = new UserToken(userId, userToken, userRefreshToken);
            isLogin = true;
        }
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        String userInfoJson = mSharedPreferences.getString(Config.CONFIG_KEY_USER_INFO, null);
        if (!TextUtils.isEmpty(userInfoJson)) {
            mUserInfo = JsonParse.fromJson(userInfoJson, UserInfo.class);
        }
    }

    /**
     * 持久化UserToken
     *
     * @param userToken
     */
    private void writeUserToken(UserToken userToken) {
        if (userToken == null) {
            return;
        }
        mUserToken = userToken;
        mSharedPreferences.edit()
                .putString(Config.CONFIG_KEY_USER_ID, mUserToken.getUserId())
                .putString(Config.CONFIG_KEY_USER_TOKEN, mUserToken.getUserToken())
                .putString(Config.CONFIG_KEY_USER_REFRESH_TOKEN, mUserToken.getUserRefreshToken())
                .apply();
    }

    /**
     * 清除UserToken
     */
    private void clearUserToken() {
        mUserToken = null;
        mSharedPreferences.edit()
                .remove(Config.CONFIG_KEY_USER_ID)
                .remove(Config.CONFIG_KEY_USER_TOKEN)
                .remove(Config.CONFIG_KEY_USER_REFRESH_TOKEN)
                .apply();
    }

    /**
     * 清除用户信息
     */
    private void clearUserInfo() {
        mUserInfo = null;
        mSharedPreferences.edit()
                .remove(Config.CONFIG_KEY_USER_INFO)
                .apply();
    }

    public class LoginExpiredException extends RuntimeException {

    }
}
