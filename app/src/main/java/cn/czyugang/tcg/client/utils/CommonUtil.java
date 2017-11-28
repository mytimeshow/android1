package cn.czyugang.tcg.client.utils;

import java.util.List;

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
}