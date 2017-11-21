package cn.czyugang.tcg.client.utils.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import cn.czyugang.tcg.client.utils.LogRui;

/**
 * Created by ruiaa on 2017/8/28.
 */

public class PhoneUtil {
    private static Context context;
    public static void register(Context appContext){
        context=appContext;
    }

    /**
     * 获取IMEI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * 手机序列号，用于在手机网络中识别每一部独立的手机，是国际上公认的手机标志序号
     *
     * @return IMEI码
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI() {
        try{
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm==null) return "no permission";
            if (tm.getDeviceId().equals("")) return "no permission";
            return  tm.getDeviceId();
        }catch (Exception e){
            LogRui.e("getIMEI####",e);
        }
        return "no permission";
    }

    /**
     * 获取IMSI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * 公众陆地移动电话网（PLMN）中用于唯一识别移动用户的一个号码。在GSM网络，这个号码通常被存放在SIM卡中
     *
     * @return IMSI码
     */
    @SuppressLint("HardwareIds")
    public static String getIMSI() {
        try{
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm != null ? tm.getSubscriberId() : null;
        }catch (Exception e){
            LogRui.e("getIMSI####",e);
        }
        return "no permission";
    }

    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    public static boolean isRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su")
                || canExecuteCommand("busybox which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String info = in.readLine();
            if (info != null) return true;
            return false;
        } catch (Exception e) {
            //do noting
        } finally {
            if (process != null) process.destroy();
        }
        return false;
    }


    /*
    *
    *     文件路径 from uri
    *
    * */
    private String parseFilePath(Uri uri, Context context) {
        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }

        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
}
