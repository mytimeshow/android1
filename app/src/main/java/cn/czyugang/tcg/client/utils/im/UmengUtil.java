package cn.czyugang.tcg.client.utils.im;

import android.app.Application;
import android.content.Context;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;

/**
 * @author ruiaa
 * @date 2018/1/10
 */

public class UmengUtil {

    public static void init(Application application){
        if (true) return;
        final PushAgent mPushAgent = PushAgent.getInstance(application);
        mPushAgent.setDebugMode(true);
        /*
        * UmengNotificationClickHandler是在BroadcastReceiver中被调用，
        * 因此若需启动Activity，需为Intent添加Flag：Intent.FLAG_ACTIVITY_NEW_TASK，否则无法启动Activity。
        * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                LogRui.i("dealWithCustomAction####",msg.toString());
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        //注册推送服务，每次调用register方法都会回调该接口
        /*
        * 请勿在调用register方法时做进程判断处理（主进程和channel进程均需要调用register方法才能保证长连接的正确建立）。
        * 若有需要，可以在Application的onCreate方法中创建一个子线程，并把mPushAgent.register这一行代码放到该子线程中去执行
        * （请勿将PushAgent.getInstance(this)放到子线程中）。
        * device token是友盟+生成的用于标识设备的id，长度为44位，不能定制和修改。同一台设备上不同应用对应的device token不一样。
        * 如需手动获取device token，可以调用mPushAgent.getRegistrationId()方法（需在注册成功后调用）。
        * */
        new Thread(()->{
            mPushAgent.register(new IUmengRegisterCallback() {

                @Override
                public void onSuccess(String deviceToken) {
                    //注册成功会返回device token
                    LogRui.d("****onSuccess####",deviceToken);

                    setTagAndAlias(mPushAgent);
                }

                @Override
                public void onFailure(String s, String s1) {
                    LogRui.e("onFailure####"+s+"    "+s1);
                }
            });
        }).start();


    }

    public static void doOnActivityCreate(Context context){
        //PushAgent.getInstance(context).onAppStart();
    }

    private static void setTagAndAlias(PushAgent mPushAgent){

        /*
        *   用户登录后的情况
        *   alias为下发的mqUser的clientId
        *   tag标签为mqUser的topic
        *   推送登录请清空alias、tag，并重新在标签加入 notification
        *
        *   没有登录情况
        *   不设置alias
        *   tag标签统一加入为 notification
        * */

        MqUserInfo mqUserInfo= AppKeyStorage.getMqUserInfo();
        String[] tags=null;
        if (mqUserInfo!=null){
            tags=new String[mqUserInfo.topic.size()];
            mqUserInfo.topic.toArray(tags);
        }else {
            tags=new String[]{"notification"};
        }

        //删除之前添加的所有标签，重置为新标签。
        mPushAgent.getTagManager().update(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                LogRui.d("****onMessage####",result);
            }
        }, tags);

        //添加id
        if (mqUserInfo!=null){
            mPushAgent.addAlias(mqUserInfo.clientId, "tcg", new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean isSuccess, String message) {
                    LogRui.d("****onMessage####",message);
                }
            });
        }
    }
}
