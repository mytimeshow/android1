package cn.czyugang.tcg.client.utils;

import android.support.annotation.DimenRes;
import android.view.View;
import android.view.ViewGroup;

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
}