package cn.czyugang.tcg.client.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 封装了腾讯Api的一些方法 <br>
 * 如果要成功接收到回调，需要在调用接口的界面的onActivityResult方法中调用 <br>
 * Tencent.onActivityResultData(requestCode,resultCode,data,listener);
 *
 * @author hong
 */

public class TencentApi implements IUiListener {
    private final String APP_ID = "1105483701";
    private final String SCOPE = "get_simple_userinfo";
    private static TencentApi mInstance;
    private Tencent mTencent;
    private Subject<JSONObject> mSubject;

    private TencentApi(Context context) {
        mTencent = Tencent.createInstance(APP_ID, context);
    }

    public static TencentApi init(Context context) {
        mInstance = new TencentApi(context);
        return mInstance;
    }

    public static TencentApi getInstance() {
        return mInstance;
    }

    /**
     * 授权
     *
     * @param activity
     * @return
     */
    public Observable<JSONObject> auth(Activity activity) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        mTencent.login(activity, SCOPE, this);
        return mSubject.take(1);
    }

    /**
     * 授权
     *
     * @param fragment
     * @return
     */
    public Observable<JSONObject> auth(Fragment fragment) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        mTencent.login(fragment, SCOPE, this);
        return mSubject.take(1);
    }

    /**
     * 登录
     *
     * @param openId
     * @param accessToken
     * @param expiresIn
     * @return
     */
    public boolean signIn(String openId, String accessToken, String expiresIn) {
        mTencent.setOpenId(openId);
        mTencent.setAccessToken(accessToken, expiresIn);
        return mTencent.isSessionValid();
    }

    /**
     * 注销当前用户
     */
    public void logout(Context context) {
        mTencent.logout(context);
    }

    /**
     * 获取用户信息 <br>
     * "nickname":"昵称","gender":"性别","province":"省","city":"城市" <br>
     * "figureurl_qq_1":"40x40像素QQ头像","figureurl_qq_2":"100*100像素QQ头像" <br>
     * "figureurl":"30x30像素QQ空间头像","figureurl_1":"50x50像素QQ空间头像","figureurl_2":
     * "100x100像素QQ空间头像"
     *
     * @return
     */
    public Observable<JSONObject> getUserInfo(Context context) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        new UserInfo(context, mTencent.getQQToken()).getUserInfo(this);
        return mSubject.take(1);
    }

    /**
     * 分享到QQ
     *
     * @param activity
     * @param url      分享链接（必填）
     * @param imageUrl 分享的图片url或本地路径（选填）
     * @param title    标题（必填,最长30个字符）
     * @param summary  内容摘要（选填,最长40个字符）
     * @return
     */
    public Observable<JSONObject> shareToQQ(Activity activity, String url, String imageUrl, String title, String summary) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);// 设置点击跳转链接（必填）
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);// 设置分享的图片url或本地路径（选填）
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 设置标题（必填,最长30个字符）
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 设置内容摘要（选填,最长40个字符）
        mTencent.shareToQQ(activity, params, this);
        return mSubject.take(1);
    }

    /**
     * 分享图片到QQ
     *
     * @param activity
     * @param imageLocalUrl 分享的图片本地路径（必填,不支持网络图片）
     * @return
     */
    public Observable<JSONObject> shareImageToQQ(Activity activity, String imageLocalUrl) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageLocalUrl);// 设置分享的图片本地路径（必填）
        mTencent.shareToQQ(activity, params, this);
        return mSubject.take(1);
    }

    /**
     * 分享音乐到QQ
     *
     * @param activity
     * @param audioUrl 分享的音乐url（必填,不支持本地文件）
     * @param url      分享链接（必填）
     * @param imageUrl 分享的图片链接（选填）
     * @param title    标题（必填,最长30个字符）
     * @param summary  内容摘要（选填,最长40个字符）
     * @return
     */
    public Observable<JSONObject> shareAudioToQQ(Activity activity, String audioUrl, String url, String imageUrl, String title, String summary) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);// 设置分享的音乐url（必填）
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);// 设置点击跳转链接（必填）
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);// 设置分享的图片url或本地路径（选填）
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 设置标题（必填,最长30个字符）
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 设置内容摘要（选填,最长40个字符）
        mTencent.shareToQQ(activity, params, this);
        return mSubject.take(1);
    }

    /**
     * 分享APP到QQ（有安装时点击直接打开APP，没安装时点击跳转到下载页面）
     *
     * @param activity
     * @param imageUrl 分享的图片url（选填）
     * @param title    标题（必填,最长30个字符）
     * @param summary  内容摘要（选填,最长40个字符）
     * @return
     */
    public Observable<JSONObject> shareAppToQQ(Activity activity, String imageUrl, String title, String summary) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);// 设置分享的图片url或本地路径（选填）
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 设置标题（必填,最长30个字符）
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 设置内容摘要（选填,最长40个字符）
        mTencent.shareToQQ(activity, params, this);
        return mSubject.take(1);
    }

    /**
     * 分享到QQ空间
     *
     * @param activity
     * @param url      分享链接（必填）
     * @param imageUrl 分享的图片url（选填）
     * @param title    标题（必填,最长200个字符）
     * @param summary  内容摘要（选填,最长600个字符）
     * @return
     */
    public Observable<JSONObject> shareToQzone(Activity activity, String url, String imageUrl, String title, String summary) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);// 设置点击跳转链接（必填）
        ArrayList<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(imageUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrlList);// 设置分享的图片url或本地路径（选填）
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 设置标题（必填,最长200个字符）
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);// 设置内容摘要（选填,最长600个字符）
        mTencent.shareToQzone(activity, params, this);
        return mSubject.take(1);
    }

    /**
     * 分享APP到QQ空间（有安装时点击直接打开APP，没安装时点击跳转到下载页面）
     *
     * @param activity
     * @param imageUrl 分享的图片链接（选填）
     * @param title    标题（必填,最长200个字符）
     * @param summary  内容摘要（选填,最长600个字符）
     * @return
     */
    public Observable<JSONObject> shareAppToQzone(Activity activity, String imageUrl, String title, String summary) {
        mSubject = PublishSubject.<JSONObject>create().toSerialized();
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_APP);
        ArrayList<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(imageUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrlList);// 设置分享的图片url或本地路径（选填）
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 设置标题（必填,最长200个字符）
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);// 设置内容摘要（选填,最长600个字符）
        mTencent.shareToQzone(activity, params, this);
        return mSubject.take(1);
    }

    /**
     * 要接收回调，需要onActivityResult调用该方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    @Override
    public void onComplete(Object o) {
        mSubject.onNext((JSONObject) o);
    }

    @Override
    public void onError(UiError uiError) {
        mSubject.onError(new Throwable(uiError.errorMessage));
    }

    @Override
    public void onCancel() {
        mSubject.onComplete();
    }
}
