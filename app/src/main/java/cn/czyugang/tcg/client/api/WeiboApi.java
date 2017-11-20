package cn.czyugang.tcg.client.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 封装了微博Api的一些方法
 *
 * @author hong
 */

public class WeiboApi implements WeiboAuthListener, RequestListener, IWeiboHandler.Response {
    private final String APP_KEY = "218077990";
    private final String WEIBO_URL = "https://api.weibo.com/";
    private final String REDIRECT_URL = WEIBO_URL + "oauth2/default.html";
    private final String USER_URL = WEIBO_URL + "2/users/show.json";
    private final String SCOPE = "";

    private static WeiboApi mInstance;
    private IWeiboShareAPI mWeiboShareAPI;
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private AsyncWeiboRunner mAsyncWeiboRunner;
    private Subject<Object> mSubject;

    private WeiboApi(Context context) {
        mAuthInfo = new AuthInfo(context, APP_KEY, REDIRECT_URL, SCOPE);
        mAsyncWeiboRunner = new AsyncWeiboRunner(context);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, APP_KEY);
        mWeiboShareAPI.registerApp();
    }

    public static WeiboApi init(Context context) {
        mInstance = new WeiboApi(context);
        return mInstance;
    }

    public static WeiboApi getInstance() {
        return mInstance;
    }

    /**
     * 授权
     *
     * @param activity
     */
    public Observable<Bundle> auth(Activity activity) {
        mSubject = PublishSubject.create().toSerialized();
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mSsoHandler.authorize(this);
        return mSubject.ofType(Bundle.class).take(1);
    }

    /**
     * 获取用户信息
     *
     * @param accessToken
     * @return
     */
    public Observable<String> getUserInfo(Oauth2AccessToken accessToken) {
        mSubject = PublishSubject.create().toSerialized();
        WeiboParameters params = new WeiboParameters(APP_KEY);
        params.put("access_token", accessToken.getToken());
        params.put("uid", accessToken.getUid());
        mAsyncWeiboRunner.requestAsync(USER_URL, params, "GET", this);
        return mSubject.ofType(String.class).take(1);
    }

    /**
     * 分享
     *
     * @param activity
     * @param content
     * @param bitmap
     * @return
     */
    public Observable<BaseResponse> share(Activity activity, String content, Bitmap bitmap) {
        mSubject = PublishSubject.create().toSerialized();
        //分享的图片
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        TextObject textObject = new TextObject();
        textObject.text = content;

        WeiboMultiMessage msg = new WeiboMultiMessage();
        msg.imageObject = imageObject;
        msg.textObject = textObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = msg;
        if (mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.sendRequest(activity, request);
        } else {
            mWeiboShareAPI.sendRequest(activity, request, mAuthInfo, "", new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {
                }

                @Override
                public void onWeiboException(WeiboException e) {
                }

                @Override
                public void onCancel() {
                }
            });
        }
        return mSubject.ofType(BaseResponse.class).take(1);
    }

    /**
     * 授权登陆时需要重写onActivityResult调用该方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 提供给WBEntryActivity调用
     *
     * @param intent
     */
    public void handleWeiboResponse(Intent intent) {
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * 授权回调
     *
     * @param bundle
     */
    @Override
    public void onComplete(Bundle bundle) {
        mSubject.onNext(bundle);
    }

    /**
     * 获取用户信息回调
     *
     * @param s
     */
    @Override
    public void onComplete(String s) {
        mSubject.onNext(s);
    }

    @Override
    public void onWeiboException(WeiboException e) {
        mSubject.onError(e);
    }

    @Override
    public void onCancel() {
        mSubject.onComplete();
    }

    /**
     * 分享回调
     *
     * @param baseResponse
     */
    @Override
    public void onResponse(BaseResponse baseResponse) {
        mSubject.onNext(baseResponse);
    }
}
