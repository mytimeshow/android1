package cn.czyugang.tcg.client.utils.storage;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.entity.TrolleyGoods;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.store.SearchActivity;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.im.MqUserInfo;

/**
 * Created by ruiaa on 2017/8/8.
 */

public class AppKeyStorage {
    /*
    *   搜索历史
    * */
    public static List<String> getSearchHistory(int type) {
        return KeyStorage.get(getSearchHistoryKey(type), new ArrayList<String>());
    }

    public static void saveSearchHistory(String searchWord,int type) {
        if (searchWord==null||searchWord.equals("")) return;
        if (searchWord.length()>10) searchWord=searchWord.substring(0,10);
        List<String> history = KeyStorage.get(getSearchHistoryKey(type), new ArrayList<String>());
        if (history.size()>10) history.remove(10);
        history.add(0,searchWord);
        KeyStorage.put(getSearchHistoryKey(type),history);
    }

    public static void clearSearchHistory(int type){
        KeyStorage.delete(getSearchHistoryKey(type));
    }

    public static String getSearchHistoryKey(int type){
        if (type== SearchActivity.SEARCH_STORE) return "SearchStoreHistory";
        if (type== SearchActivity.SEARCH_INFORM) return "SearchInformHistory";
        return "SearchHistory";
    }

    /*
    *   推广有礼  首次引导
    * */
    public static boolean isFirstOpenPromoterIntro(){
        return KeyStorage.get("isFirstOpenPromoterIntro",true);
    }

    public static void saveHadOpenPromoterIntro(){
        KeyStorage.put("isFirstOpenPromoterIntro",false);
    }

    /*
    *   本地购物车
    * */
    public static TrolleyStore getTrolleyStore(String storeId){
        Map<String,TrolleyStore> trolleyStoreMap=KeyStorage.get("TrolleyStoreMap");
        if (trolleyStoreMap==null||!trolleyStoreMap.containsKey(storeId)) return new TrolleyStore();
        return trolleyStoreMap.get(storeId);
    }

    public static void saveTrolleyStore(String storeId,TrolleyStore trolleyStore){
        Map<String,TrolleyStore> trolleyStoreMap=KeyStorage.get("TrolleyStoreMap");
        if (trolleyStoreMap==null) trolleyStoreMap=new HashMap<>();
        trolleyStoreMap.put(storeId,trolleyStore);
        KeyStorage.put("TrolleyStoreMap",trolleyStoreMap);
    }

    public static void clearTrolleyDeleteFlag(String storeId){
        TrolleyStore trolleyStore=getTrolleyStore(storeId);
        if (!trolleyStore.trolleyGoodsMap.isEmpty()){
            List<String> keys=new ArrayList<>(trolleyStore.trolleyGoodsMap.keySet());
            for(String k:keys){
                if (trolleyStore.trolleyGoodsMap.get(k).hadDeleted())
                    trolleyStore.trolleyGoodsMap.remove(k);
            }
        }
        saveTrolleyStore(storeId,trolleyStore);
    }

    public static void clearTrolleyAfterSettle(String storeId,List<String> trolleyIds){
        TrolleyStore trolleyStore=getTrolleyStore(storeId);
        if (!trolleyStore.trolleyGoodsMap.isEmpty()){
            for(String id:trolleyIds){
                for(TrolleyGoods t:trolleyStore.trolleyGoodsMap.values()){
                    if (id.equals(t.trolleyId)) {
                        LogRui.i("clearTrolleyAfterSettle####",id);
                        trolleyStore.trolleyGoodsMap.remove(trolleyStore.getTrolleyGoodsKey(t));
                        break;
                    }
                }
            }
        }
        saveTrolleyStore(storeId,trolleyStore);
    }

    public static void clearTrolleyAfterSettle(String storeId,String trolleyId){
        TrolleyStore trolleyStore=getTrolleyStore(storeId);
        if (!trolleyStore.trolleyGoodsMap.isEmpty()){
            for(TrolleyGoods t:trolleyStore.trolleyGoodsMap.values()){
                if (trolleyId.equals(t.trolleyId)) {
                    trolleyStore.trolleyGoodsMap.remove(trolleyStore.getTrolleyGoodsKey(t));
                    break;
                }
            }
        }
        saveTrolleyStore(storeId,trolleyStore);
    }

    public static void clearTrolleyStore(String storeId){
        Map<String,TrolleyStore> trolleyStoreMap=KeyStorage.get("TrolleyStoreMap");
        if (trolleyStoreMap==null) trolleyStoreMap=new HashMap<>();
        if (storeId==null) {
            trolleyStoreMap.clear();
        }else {
            trolleyStoreMap.remove(storeId);
        }
        KeyStorage.put("TrolleyStoreMap",trolleyStoreMap);
    }

    /*
    *   记录用户的mqtt账号
    * */
    public static void saveMqUserInfo(@Nullable MqUserInfo mqUserInfo){
        if (mqUserInfo!=null) KeyStorage.put("MqUserInfo",mqUserInfo);
    }

    public static void clearMqUserInfo(){
        KeyStorage.delete("MqUserInfo");
    }

    public static MqUserInfo getMqUserInfo(){
        return KeyStorage.get("MqUserInfo");
    }
}
