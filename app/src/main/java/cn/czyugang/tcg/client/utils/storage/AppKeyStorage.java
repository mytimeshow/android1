package cn.czyugang.tcg.client.utils.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruiaa on 2017/8/8.
 */

public class AppKeyStorage {
    /*
    *   搜索历史
    * */
    public static List<String> getSearchHistory() {
        return KeyStorage.get("SearchHistory", new ArrayList<String>());
    }

    public static void saveSearchHistory(String searchWord) {
        if (searchWord==null||searchWord.equals("")) return;
        if (searchWord.length()>10) searchWord=searchWord.substring(0,10);
        List<String> history = KeyStorage.get("SearchHistory", new ArrayList<String>());
        if (history.size()>10) history.remove(10);
        history.add(0,searchWord);
        KeyStorage.put("SearchHistory",history);
    }

    public static void clearSearchHistory(){
        KeyStorage.delete("SearchHistory");
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
}
