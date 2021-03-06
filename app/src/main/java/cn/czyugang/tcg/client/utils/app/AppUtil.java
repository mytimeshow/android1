package cn.czyugang.tcg.client.utils.app;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by ruiaa on 2016/11/12.
 */

public class AppUtil {

    private static Context context;
    public static int Status_Bar_Height;


    public static void register(Context appContext) {
        context = appContext.getApplicationContext();
        Status_Bar_Height = getStatusBarHeight();
    }


    public static void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int id) {
        Toast.makeText(context, ResUtil.getString(id), Toast.LENGTH_SHORT).show();
    }


    /*
     * 粘贴板
     */
    public static void copyToClipBoard(String textCopy, String successToast) {
        ClipData clipData = ClipData.newPlainText(ResUtil.getString(R.string.app_name), textCopy);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        if (successToast != null && !successToast.isEmpty()) {
            Toast.makeText(context, successToast, Toast.LENGTH_SHORT).show();
        }
    }

    public static void copyToClipBoard(String textCopy, @StringRes int successToast) {
        copyToClipBoard(textCopy, ResUtil.getString(successToast));
    }

    public static void copyToClipBoard(String textCopy) {
        copyToClipBoard(textCopy, "已复制到粘贴板");
    }


    /*
    *  电话 短信
    * */
    public static void call(Context context, String phone) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "app没有打电话权限", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
        }
    }

    public static void sms(Context context, String phone, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }


    /*
     * 软键盘 显示与隐藏
     */
    public static void hideKeyBoard(View target) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }

    public static void showKeyBoard(final View target, long delay) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (delay == 0) {
                target.requestFocus();
                imm.showSoftInput(target, 0);
            } else {
                Observable.just(1)
                        .delay(delay, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            target.requestFocus();
                            imm.showSoftInput(target, 0);
                        });
            }
        }
    }


    public static void showKeyBoard(View target) {
        showKeyBoard(target, 200L);
    }

    public static void toggleSoftInput(View target) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInputFromWindow(target.getWindowToken(), 0, 0);
        }
    }


    /*
     * 屏幕尺寸
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.heightPixels;
    }

    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }

    public static int getStatusBarHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }
        int result = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

/*    public void setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置根布局的内边距
            //window.getDecorView().setPadding(0,getStatusBarHeight(), 0, 0);
            //appBarLayout.setPadding(0,getStatusBarHeight(),0,0);

            // 创建TextView
            // 获得根视图并把TextView加进去。
*//*          TextView textView = new TextView(this);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
            textView.setBackgroundColor(ResUtil.getColor(R.color.colorPrimary));
            textView.setLayoutParams(lParams);
            appBarLayout.getParent().addView(textView,0);*//*
        }
    }

    // 获取手机状态栏高度
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 获取ActionBar的高度
    public int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }*/
}
