package cn.czyugang.tcg.client.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;

import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class ShareUtil {

    /*
    *
    *       系统分享
    *
    * */
    public static void shareImage(Context context, String title, Uri uri) {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }


    public static void shareText(Context context, String subjectText, String extraText) {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, subjectText));
    }

    /*
    *
    *       微信分享：微信好友、朋友圈
    *
    * */
    //https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN
    private static IWXAPI WxApi=null;
    private static final String WECHAT_APP_ID="";
    private static IWXAPI getWxApi() {
        if (WxApi==null) {
            WxApi = WXAPIFactory.createWXAPI(MyApplication.getContext(),WECHAT_APP_ID, true);
            WxApi.registerApp(WECHAT_APP_ID);
        }
        return WxApi;
    }
    public static void shareImgWeixin(Bitmap bmp, boolean isToSession) {
        if (bmp == null) return;

        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 5, bmp.getHeight() / 5, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);  //设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");

        req.message = msg;
        req.scene = isToSession ?
                SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        getWxApi().sendReq(req);
    }

    public static void shareTextWeixin(String text, boolean isToSession) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");

        req.message = msg;
        req.scene = isToSession ?
                SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        getWxApi().sendReq(req);
    }

    public static void shareUrlWeixin(Context context, String url, String title, String description, Bitmap bmp, boolean requestRecycle, boolean isToSession) {
        if (bmp == null||bmp.isRecycled()) {
            LogRui.e("shareUrlWeixin####");
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        if (requestRecycle) bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isToSession ?
                SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        getWxApi().sendReq(req);

    }

    public static void shareUrlWeixin(Context context, String url, String title, String description, @DrawableRes int img, boolean isToSession) {
        shareUrlWeixin(context, url, title, description, BitmapFactory.decodeResource(context.getResources(), img), true, isToSession);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

/*        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;*/
    }


    /*
    *
    *       QQ分享：QQ好友、QQ空间
    *
    * */
    private static final String QQ_APP_ID="";
    private static  Tencent tencent=null;
    public static Tencent getTencentApi() {
        if (tencent==null){
            tencent=Tencent.createInstance(QQ_APP_ID, MyApplication.getContext());
        }
        return tencent;
    }
    public static void shareUrlQQ(Activity activity, IUiListener iUiListener,
                                  String url, String title, String description, String imgUrl) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ResUtil.getString(R.string.app_name));
        getTencentApi().shareToQQ(activity, params, iUiListener);
    }

    public static void shareImgQQ(Activity activity, IUiListener iUiListener, String localUrl) {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,localUrl);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        //params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ResUtil.getString(R.string.app_name)+ QQ_APP_ID);
        getTencentApi().shareToQQ(activity, params, iUiListener);
    }


    /*
    *
    *       微博分享
    *
    * */

}
