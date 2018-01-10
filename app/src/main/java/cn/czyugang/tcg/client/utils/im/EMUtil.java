package cn.czyugang.tcg.client.utils.im;

import android.app.Application;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;

/**
 * @author ruiaa
 * @date 2018/1/10
 *
 *  环信接口
 */

public class EMUtil {

    public static void init(Application applicationContext){
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        //options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        //options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        //options.setAutoDownloadThumbnail(true);

        //初始化
        EMClient.getInstance().init(applicationContext, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        login();
    }

    public static void login(){
        MqUserInfo mqUserInfo= AppKeyStorage.getMqUserInfo();
        if (mqUserInfo!=null) login(mqUserInfo.username,mqUserInfo.password);
    }

    private static void login(String userName,String password){
        EMClient.getInstance().login(userName,password,new EMCallBack() {
            @Override
            public void onSuccess() {
                //保证进入主页面后本地会话和群组都 load 完毕
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                LogRui.d("****onSuccess####", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogRui.d("****onError####", "登录聊天服务器失败！");
            }
        });
    }

    public static void logout(){
        EMClient.getInstance().logout(true);

        /*EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });*/
    }

}
