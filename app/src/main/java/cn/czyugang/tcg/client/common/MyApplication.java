package cn.czyugang.tcg.client.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;

import cn.czyugang.tcg.client.api.TencentApi;
import cn.czyugang.tcg.client.api.WXApi;
import cn.czyugang.tcg.client.api.WeiboApi;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.storage.FileStorage;
import cn.czyugang.tcg.client.utils.storage.KeyStorage;

/**
 * Created by wuzihong on 2017/8/29.
 * Application
 */

public class MyApplication extends Application {
    public static int sVersionCode;//当前版本号
    public static String sVersionName;//当前版本名
    private static Application application;
    private static MyApplication myApplication;
    public Object activityTransferData = null;//用于activity跳转时中转大数据

    public static Application getContext() {
        return application;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        myApplication = this;
        ResUtil.register(application);
        KeyStorage.register(application);
        FileStorage.register(application);
        AppUtil.register(application);

        Context context = getApplicationContext();
        getVersionInfo(context);
        //初始化全局配置
        Config.init(context);
        //初始化网络框架
        Network.init(Config.getApiUrl());
        AppOAuth.init(context);
        UserOAuth.init(context);
        //初始化位置
        LocationManager.init(context);
        //Fresco初始化 使用OkHttp加载图片
        Fresco.initialize(context, OkHttpImagePipelineConfigFactory.newBuilder(context, Network.getInstance().getOkHttpClient()).build());
        //腾讯接口
        TencentApi.init(context);
        //微信接口
        WXApi.init(context);
        //微博接口
        WeiboApi.init(context);
        //路由初始化
        if (Config.isDebug()) {    // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }

    /**
     * 获取版本信息
     *
     * @param context
     */
    private void getVersionInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            sVersionCode = packageInfo.versionCode;
            sVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
