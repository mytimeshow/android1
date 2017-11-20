package cn.czyugang.tcg.client.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import cn.czyugang.tcg.client.entity.StaticDict;

/**
 * Created by wuzihong on 2017/9/25.
 * 字典查询工具
 */

public class DictUtil {
    /**
     * 根据key获取基本类型对象
     *
     * @param jsonObject
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObject(JSONObject jsonObject, String key) {
        try {
            return (T) jsonObject.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据key获取实体对象
     *
     * @param jsonObject
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getObject(JSONObject jsonObject, String key, java.lang.reflect.Type type) {
        Object values = getObject(jsonObject, key);
        if (values instanceof JSONObject || values instanceof JSONArray) {
            return JsonParse.fromJson(values.toString(), type);
        } else {
            return null;
        }
    }

    /**
     * 获取字典列表
     *
     * @param jsonObject
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getDict(JSONObject jsonObject, String key, java.lang.reflect.Type type) {
        return getObject(jsonObject, key, new JsonParse.Type(List.class, type));
    }

    /**
     * 获取静态字典列表
     *
     * @param jsonObject
     * @param key
     * @param <T>
     * @return
     */
    public static <T> List<T> getStaticDict(JSONObject jsonObject, String key) {
        return getDict(jsonObject, key, StaticDict.class);
    }

    /**
     * 查找字典列表中key的值等于id的实体类
     *
     * @param dictList
     * @param key
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T lookupDict(List<T> dictList, String key, Object id) {
        try {
            if (dictList != null) {
                for (int i = 0, length = dictList.size(); i < length; i++) {
                    T values = dictList.get(i);
                    Field keyField = values.getClass().getDeclaredField(key);
                    keyField.setAccessible(true);
                    if (id.equals(keyField.get(values))) {
                        return values;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据id查找静态字典中的值
     *
     * @param dictList
     * @param id
     * @return
     */
    public static String lookupStaticDict(List<StaticDict> dictList, String id) {
        if (dictList != null) {
            for (int i = 0, length = dictList.size(); i < length; i++) {
                StaticDict values = dictList.get(i);
                if (id.equals(values.getId())) {
                    return values.getName();
                }
            }
        }
        return id;
    }
}
