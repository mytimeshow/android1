package cn.czyugang.tcg.client.utils;

import android.support.annotation.DimenRes;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.List;

import cn.czyugang.tcg.client.utils.app.ResUtil;

/**
 * Created by ruiaa on 2017/11/10.
 */

public class CommonUtil {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void move(List<?> list, int from, int to) {
        final List l = list;
        l.add(to, l.remove(from));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String toString(List<?> list){
        final List l = list;
        StringBuilder stringBuilder=new StringBuilder();
        for(Object o:l){
            stringBuilder.append(o.toString());
        }
        return stringBuilder.toString();
    }

    public static void addMarginTop(View view,@DimenRes int m){
        ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams)view.getLayoutParams();
        lp.topMargin=lp.topMargin+ ResUtil.getDimenInPx(m);
        view.setLayoutParams(lp);
    }

    public static void addMarginBottom(View view,@DimenRes int m){
        ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams)view.getLayoutParams();
        lp.bottomMargin=lp.bottomMargin+ ResUtil.getDimenInPx(m);
        view.setLayoutParams(lp);
    }

    public static void setMarginTop(View view,@DimenRes int m){
        ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams)view.getLayoutParams();
        lp.topMargin= ResUtil.getDimenInPx(m);
        view.setLayoutParams(lp);
    }

    public static void setMarginBottom(View view,@DimenRes int m){
        ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams)view.getLayoutParams();
        lp.bottomMargin= ResUtil.getDimenInPx(m);
        view.setLayoutParams(lp);
    }



    public static void setTabLayoutIndicator(TabLayout tabs, int leftMargin, int rightMargin) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (tabStrip==null) return;

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (llTab==null) return;

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = leftMargin;
            params.rightMargin = rightMargin;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}