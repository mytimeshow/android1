package cn.czyugang.tcg.client.utils.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import cn.czyugang.tcg.client.utils.LogUtil;


/**
 * Created by ruiaa on 2016/10/21.
 */

public class ShareUtil {

    public static void shareImage(Context context, String title, Uri uri) {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }


    public static void shareText(Context context, String subjectText, String extraText) {
        LogUtil.i("shareText####  "+subjectText+"\n"+extraText);
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, subjectText));
    }

    //https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN
/*    public static void shareImgWeixin(Bitmap bmp, boolean isToSession) {
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
        MyApplication.wxApi.sendReq(req);

        LogUtil.i("shareImgWeixin####");
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

*//*        ByteArrayOutputStream output = new ByteArrayOutputStream();
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

        return result;*//*
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

        MyApplication.wxApi.sendReq(req);
    }

    public static void shareUrlWeixin(Context context, String url, String title, String description, Bitmap bmp, boolean requestRecycle, boolean isToSession) {
        if (bmp == null||bmp.isRecycled()) {
            LogUtil.e("shareUrlWeixin####bmp,null");
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
        MyApplication.wxApi.sendReq(req);

        LogUtil.i("shareUrlWeixin####",url);
    }

    public static void shareUrlWeixin(Context context, String url, String title, String description, @DrawableRes int img, boolean isToSession) {
        shareUrlWeixin(context, url, title, description, BitmapFactory.decodeResource(context.getResources(), img), true, isToSession);
    }

    public static void shareUrlQQ(Tencent tencent, Activity activity, IUiListener iUiListener,
                                  String url, String title, String description, String imgUrl) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ResUtil.getString(R.string.app_name));
        tencent.shareToQQ(activity, params, iUiListener);
        LogUtil.i("shareUrlQQ####",url);
        LogUtil.i("shareUrlQQ####",imgUrl);
    }

    public static void shareImgQQ(Tencent tencent, Activity activity, IUiListener iUiListener, String localUrl) {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,localUrl);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        //params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ResUtil.getString(R.string.app_name)+ Constants.QQ_APP_ID);
        tencent.shareToQQ(activity, params, iUiListener);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    public static int SHARE_RECORD_FROM_REPORT=1;
    public static int SHARE_RECORD_FROM_AWARD=2;
    public static int SHARE_RECORD_TO_WECHAT=1;
    public static int SHARE_RECORD_TO_QUAN=2;
    public static int SHARE_RECORD_TO_QQ=3;
    public static void postShareRecord(int from,int to){
        RequestHelper.createVolley()
                .addIdCode()
                .add("from",from)
                .add("type",to)
                .post(AppConfig.URL_SHARE_RECORD,response -> {
                    LogUtil.i("postShareRecord####",response);
                });
    }*/
}
