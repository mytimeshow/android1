package cn.czyugang.tcg.client.api;

import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.GoodCategory;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class StoreApi {

    public static Observable<Response<Store>> getStoreById(String id){
        HashMap<String,Object> map=new HashMap<>();
        map.put("storeId",id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/store/apply/getOne/detailId",map)
                .map(s -> (Response<Store>) JsonParse.fromJson(s,new JsonParse.Type(Response.class,Store.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<List<GoodCategory>>> goodsCategory() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("storeId","222");
        return UserOAuth.getInstance()
                .get("api/auth/v1/store/classify/in-store/classify/search", map)
                .map(s -> (Response<List<GoodCategory>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class,GoodCategory.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
