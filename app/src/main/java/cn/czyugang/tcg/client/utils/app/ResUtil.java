package cn.czyugang.tcg.client.utils.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.widget.TextView;


/**
 * Created by ruiaa on 2016/10/2.
 */

public class ResUtil {

    private static Context context;
    private static Resources resources;
    private static int widthInPx=-1;
    private static int heightInPx=-1;

    public static void register(Context appContext) {
        context = appContext.getApplicationContext();
        resources = context.getResources();
    }

    public static String getString(int stringFromR) {
        return resources.getString(stringFromR);
    }

    public static String format(int stringFromR , Object... args){
        return String.format(ResUtil.getString(stringFromR),args);
    }

    public static int getColor(int colorFromR) {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColor(colorFromR, null);
        } else {
            return resources.getColor(colorFromR);
        }
    }

    public static int getColor(int colorFromR, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColor(colorFromR, theme);
        } else {
            return resources.getColor(colorFromR);
        }
    }

    public static Drawable getDrawable(int drawableFromR) {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(drawableFromR, null);
        } else {
            return resources.getDrawable(drawableFromR);
        }
    }

    public static Drawable getDrawable(int drawableFromR, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(drawableFromR, theme);
        } else {
            return resources.getDrawable(drawableFromR);
        }
    }

    public static int getDimenInPx(int dimenFromR){
        return resources.getDimensionPixelSize(dimenFromR);
    }

    public static String getPathFromDrawableId(int drawableId) {
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + resources.getResourcePackageName(drawableId)
                + "/" + resources.getResourceTypeName(drawableId)
                + "/" + resources.getResourceEntryName(drawableId)
        );
        return uri.toString();
    }

    public static void setTextAppearance(TextView textView, @StyleRes int resId){
        if (Build.VERSION.SDK_INT>=23){
            textView.setTextAppearance(resId);
        }else {
            textView.setTextAppearance(context,resId);
        }
    }



    //#######################        尺寸转换        #####################################################################
    public static int dp2px(float dpValue) {
        if (dpValue==0) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(float pxValue) {
        if (pxValue==0) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        if (spValue==0) return 0;
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        if (pxValue==0) return 0;
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static float mm2px(float mm){
        return mm * ((float) context.getResources().getDisplayMetrics().densityDpi) /25.4f;
    }

    public static int percentToPxInWidth(float percentW) {
        return (int) (getWidthInPx() * percentW);
    }

    public static int percentToPxInHeight(float percentH) {
        return (int) (getHeightInPx() * percentH);
    }

    public static int getWidthInPx() {
        if (widthInPx == -1) {
            widthInPx = resources.getDisplayMetrics().widthPixels;
        }
        return widthInPx;
    }

    public static int getHeightInPx() {
        if (heightInPx == -1) {
            heightInPx = resources.getDisplayMetrics().heightPixels;
        }
        return heightInPx;
    }

}
