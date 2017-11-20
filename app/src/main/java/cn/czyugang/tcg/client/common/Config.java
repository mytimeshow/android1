package cn.czyugang.tcg.client.common;

import android.content.Context;

import cn.czyugang.tcg.client.utils.CLog;

/**
 * Created by wuzihong on 2017/8/29.
 * 全局配置
 */

public class Config {
    public static final int ENVIRONMENT_PROD = 0;//生存环境
    public static final int ENVIRONMENT_TEST = 1;//测试环境

    public static final String OS_TYPE = "ANDROID";//系统类型
    public static final String APK_NAME = "tcg_client.apk";//更新包名称
    public static final String API_VERSION = "1.0";//接口版本

    public static final String CONFIG_NAME = "config";//配置文件名
    public static final String CONFIG_KEY_VERSION_CODE = "version_code";//配置文件指定的应用版本号
    public static final String CONFIG_KEY_VERSION_NAME = "version_name";//配置文件指定的应用版本名
    public static final String CONFIG_KEY_ENVIRONMENT = "environment";//运行环境
    public static final String CONFIG_KEY_SHOW_NEW_FEATURE = "show_new_feature";//是否显示新特新界面
    public static final String CONFIG_KEY_NOTIFICATION_REMIND = "notification_remind";//是否接收通知提醒
    public static final String CONFIG_KEY_SOUND_REMIND = "sound_remind";//是否接收声音提醒
    public static final String CONFIG_KEY_VIBRATE_REMIND = "vibrate_remind";//是否接收震动提醒
    public static final String CONFIG_KEY_APP_ID = "app_id";
    public static final String CONFIG_KEY_APP_TOKEN = "app_token";
    public static final String CONFIG_KEY_ACCESS_TOKEN = "access_token";
    public static final String CONFIG_KEY_REFRESH_TOKEN = "refresh_token";
    public static final String CONFIG_KEY_USER_INFO = "user_info";
    public static final String CONFIG_KEY_USER_ID = "user_id";
    public static final String CONFIG_KEY_USER_TOKEN = "user_token";
    public static final String CONFIG_KEY_USER_REFRESH_TOKEN = "user_refresh_token";

    public static final int SHOW_NEW_FEATURE_VERSION_CODE = 1;//显示新特性的版本号，旧版本号低于等于该版本号，则显示新特性
    public static final int VERIFICATION_CODE_SEND_INTERVAL = 60;//验证码发送间隔时间(s)
    public static final int PAGE_SIZE = 10;//分页列表每页的数量

    private static int sEnvironment = ENVIRONMENT_TEST;//运行环境
    private static boolean sDebug;//是否是debug模式
    private static String sApiUrl;//接口连接

    /**
     * 初始化配置信息
     *
     * @param context
     */
    public static void init(Context context) {
        sEnvironment = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE).getInt(CONFIG_KEY_ENVIRONMENT, sEnvironment);
        switch (sEnvironment) {
            case ENVIRONMENT_TEST:
                sDebug = true;
                CLog.setLogLevel(CLog.LEVEL_VERBOSE);
                sApiUrl = "http://doc.api.czyugang.com/";
                break;
            default:
                sDebug = false;
                CLog.setLogLevel(CLog.LEVEL_FATAL);
                sApiUrl = "http://doc.api.czyugang.com/";
                break;
        }
    }

    public static String getApiUrl() {
        return sApiUrl;
    }

    public static boolean isDebug() {
        return sDebug;
    }
}
