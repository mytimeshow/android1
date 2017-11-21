package cn.czyugang.tcg.client.api;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class Store {
    public static Observable<Response<String>> categoryList() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("storeId","222");
        return UserOAuth.getInstance()
                .get("/api/auth/v1/store/classify/in-store/classify/search", map)
                .map(s -> (Response<String>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(String.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
