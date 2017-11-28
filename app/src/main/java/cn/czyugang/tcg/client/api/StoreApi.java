package cn.czyugang.tcg.client.api;

import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.GoodCategory;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.entity.StoreImg;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class StoreApi {

    public static Observable<Response<Store>> getStoreById(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId", id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/store/apply/getOne/detailId", map)
                .map(s -> (Response<Store>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Store.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //店铺环境图片
    public static Observable<Response<List<StoreImg>>> getStoreEnvironmentImgs(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId",id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/store/apply/pic/get", map)
                .map(s -> (Response<List<StoreImg>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, StoreImg.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //店铺商品
    public static Observable<Response<List<GoodCategory>>> getFoods(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId",id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/getProductInStore", map)
                .map(s -> (Response<List<GoodCategory>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, GoodCategory.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<List<GoodCategory>>> getGoods(String id,String classifyId,String order) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId",id);
        map.put("page",1);
        map.put("size",20);
        if (classifyId!=null) map.put("classifyId",classifyId);
        if (order!=null){
            switch (order){
                case "new":{
                    map.put("orderFields",new String[]{"new"});
                    map.put("orderTypes",new String[]{"DESC"});
                    break;
                }
                case "productSales":{
                    map.put("orderFields",new String[]{"productSales"});
                    map.put("orderTypes",new String[]{"DESC"});
                    break;
                }
                case "priceASC":{
                    map.put("orderFields",new String[]{"price"});
                    map.put("orderTypes",new String[]{"ASC"});
                    break;
                }
                case "priceDESC":{
                    map.put("orderFields",new String[]{"price"});
                    map.put("orderTypes",new String[]{"DESC"});
                    break;
                }
            }
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/getMarketProductInStore", map)
                .map(s -> (Response<List<GoodCategory>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, GoodCategory.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
