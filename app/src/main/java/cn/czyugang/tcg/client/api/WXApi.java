package cn.czyugang.tcg.client.api;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.czyugang.tcg.client.common.Network;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 封装了微信Api的一些方法
 *
 * @author hong
 */

public class WXApi implements IWXAPIEventHandler {
    private final String APP_ID = "wxffdae52c9d7702df";
    private final String APP_SECRET = "19f3ee80b8044f7b885e597da0cc15f6";
    private static WXApi mInstance;
    private IWXAPI mIWXAPI;
    private Subject<BaseResp> mSubject;

    private WXApi(Context context) {
        mIWXAPI = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mIWXAPI.registerApp(APP_ID);
    }

    public static WXApi init(Context context) {
        mInstance = new WXApi(context);
        return mInstance;
    }

    public static WXApi getInstance() {
        return mInstance;
    }

    /**
     * 提供给WXEntryActivity调用，设置微信回调的Intent和Handler
     *
     * @param intent 微信回调的Intent
     */
    public void handleIntent(Intent intent) {
        mIWXAPI.handleIntent(intent, this);
    }

    /**
     * 判断是否有安装微信客户端
     *
     * @return 是否安装微信客户端
     */
    public boolean isWXAppInstalled() {
        return mIWXAPI.isWXAppInstalled();
    }

    /**
     * 授权
     *
     * @return
     */
    public Observable<BaseResp> auth() {
        mSubject = PublishSubject.<BaseResp>create().toSerialized();
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        mIWXAPI.sendReq(req);
        return mSubject.take(1);
    }

    /**
     * 换取accessToken
     *
     * @param code
     * @return
     */
    public Observable<JSONObject> accessToken(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", APP_ID);
        params.put("secret", APP_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        return Network.getInstance().get("https://api.weixin.qq.com/sns/oauth2/access_token", params)
                .map(s -> (JSONObject) JsonParse.fromJson(s, JSONObject.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 分享文字
     *
     * @param scene 分享的位置 <br>
     *              Req.WXSceneFavorite 收藏到收藏夹 <br>
     *              Req.WXSceneSession 分享到好友 <br>
     *              Req.WXSceneTimeline 分享到朋友圈
     * @param text  分享的文字（必填）
     * @return
     */
    public Observable<BaseResp> shareText(int scene, String text) {
        mSubject = PublishSubject.<BaseResp>create().toSerialized();
        WXTextObject textObject = new WXTextObject(text);
        WXMediaMessage msg = new WXMediaMessage(textObject);
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = scene;
        mIWXAPI.sendReq(req);
        return mSubject.take(1);
    }

    /**
     * 分享图片
     *
     * @param scene  分享的位置 <br>
     *               Req.WXSceneFavorite 收藏到收藏夹 <br>
     *               Req.WXSceneSession 分享到好友 <br>
     *               Req.WXSceneTimeline 分享到朋友圈
     * @param bitmap 分享的图片（必填）
     * @return
     */
    public Observable<BaseResp> shareImage(int scene, Bitmap bitmap) {
        mSubject = PublishSubject.<BaseResp>create().toSerialized();
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage(imageObject);
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = scene;
        mIWXAPI.sendReq(req);
        return mSubject.take(1);
    }

    /**
     * 分享音乐
     *
     * @param scene       分享的位置 <br>
     *                    Req.WXSceneFavorite 收藏到收藏夹 <br>
     *                    Req.WXSceneSession 分享到好友 <br>
     *                    Req.WXSceneTimeline 分享到朋友圈
     * @param url         分享的音乐url（必填）
     * @param bitmap      分享的图片
     * @param title       标题
     * @param description 内容摘要
     * @return
     */
    public Observable<BaseResp> shareMusic(int scene, String url, Bitmap bitmap, String title, String description) {
        mSubject = PublishSubject.<BaseResp>create().toSerialized();
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = url;
        WXMediaMessage msg = new WXMediaMessage(musicObject);
        msg.title = title;
        msg.description = description;
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = scene;
        mIWXAPI.sendReq(req);
        return mSubject.take(1);
    }

    /**
     * 分享视频
     *
     * @param scene       分享的位置 <br>
     *                    Req.WXSceneFavorite 收藏到收藏夹 <br>
     *                    Req.WXSceneSession 分享到好友 <br>
     *                    Req.WXSceneTimeline 分享到朋友圈
     * @param url         分享的视频url（必填）
     * @param bitmap      分享的图片
     * @param title       标题
     * @param description 内容摘要
     * @return
     */
    public Observable<BaseResp> shareVideo(int scene, String url, Bitmap bitmap, String title, String description) {
        mSubject = PublishSubject.<BaseResp>create().toSerialized();
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = url;
        WXMediaMessage msg = new WXMediaMessage(videoObject);
        msg.title = title;
        msg.description = description;
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = scene;
        mIWXAPI.sendReq(req);
        return mSubject.take(1);
    }

    /**
     * 分享网页
     *
     * @param scene       分享的位置 <br>
     *                    Req.WXSceneFavorite 收藏到收藏夹 <br>
     *                    Req.WXSceneSession 分享到好友 <br>
     *                    Req.WXSceneTimeline 分享到朋友圈
     * @param url         分享的网页url（必填）
     * @param bitmap      分享的图片
     * @param title       标题
     * @param description 内容摘要
     * @return
     */
    public Observable<BaseResp> shareWebpage(int scene, String url, Bitmap bitmap, String title, String description) {
        mSubject = PublishSubject.<BaseResp>create().toSerialized();
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = title;
        msg.description = description;
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = scene;
        mIWXAPI.sendReq(req);
        return mSubject.take(1);
    }

    /**
     * 微信支付
     *
     * @param prepayId  预支付交易会话ID
     * @param nonceStr  随机数
     * @param timeStamp 时间戳
     * @param sign      签名
     * @return
     */
    public Observable<BaseResp> pay(String partnerId, String prepayId, String nonceStr, String timeStamp, String sign) {
        mSubject = PublishSubject.<BaseResp>create().toSerialized();
        PayReq req = new PayReq();
        req.appId = APP_ID;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.sign = sign;
        mIWXAPI.sendReq(req);
        return mSubject.take(1);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        mSubject.onNext(baseResp);
    }
}
