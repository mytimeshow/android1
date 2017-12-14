package cn.czyugang.tcg.client.api;

import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.GoodCategory;
import cn.czyugang.tcg.client.entity.GoodsResponse;
import cn.czyugang.tcg.client.entity.GoodsSpecResponse;
import cn.czyugang.tcg.client.entity.OrderPreSettleResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.entity.StoreImg;
import cn.czyugang.tcg.client.entity.TrolleyCheckResponse;
import cn.czyugang.tcg.client.entity.TrolleyGoods;
import cn.czyugang.tcg.client.entity.TrolleyPost;
import cn.czyugang.tcg.client.entity.TrolleyResponse;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.utils.AmapLocationUtil;
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
        map.put("storeId", id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/store/apply/pic/get", map)
                .map(s -> (Response<List<StoreImg>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, StoreImg.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //店铺商品  外卖
    public static Observable<Response<List<GoodCategory>>> getFoods(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId", id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/getProductInStore", map)
                .map(s -> (Response<List<GoodCategory>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, GoodCategory.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //店铺商品  商超
    public static Observable<GoodsResponse> getGoods(String id, String classifyId, String order) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId", id);
        map.put("page", 1);
        map.put("size", 20);
        if (classifyId != null) map.put("classifyId", classifyId);
        if (order != null) {
            switch (order) {
                case "new": {
                    map.put("orderFields", "new");
                    map.put("orderTypes", "DESC");
                    break;
                }
                case "productSales": {
                    map.put("orderFields", "productSales");
                    map.put("orderTypes", "DESC");
                    break;
                }
                case "priceASC": {
                    map.put("orderFields", "price");
                    map.put("orderTypes", "ASC");
                    break;
                }
                case "priceDESC": {
                    map.put("orderFields", "price");
                    map.put("orderTypes", "DESC");
                    break;
                }
            }
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/getMarketProductInStore", map)
                .map(s -> (GoodsResponse) JsonParse.fromJson(s, GoodsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //商品规格
    public static Observable<GoodsSpecResponse> getGoodsSpec(String storeInventoryId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeInventoryId", storeInventoryId);
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/getAttributes", map)
                .map(s -> (GoodsSpecResponse) JsonParse.fromJson(s, GoodsSpecResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //查询购物车
    public static Observable<TrolleyResponse> getTrolley(String storeId) {
        HashMap<String, Object> map = new HashMap<>();
        if (storeId != null) map.put("storeId", storeId);
        map.put("location", AmapLocationUtil.getXY());
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/shoppingCart/get", map)
                .map(s -> (TrolleyResponse) JsonParse.fromJson(s, TrolleyResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //同步购物车
    public static Observable<Response<List<TrolleyGoods>>> syncTrolleyGoods(TrolleyStore trolleyStore) {
        HashMap<String, Object> innerMap = new HashMap<>();
        innerMap.put("localShoppingCartVOList", TrolleyPost.newTrolleyPostList(trolleyStore));
        return UserOAuth.getInstance()
                .post("api/auth/v1/product/shopping/shoppingCart/sync", innerMap)
                .map(s -> (Response<List<TrolleyGoods>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, TrolleyGoods.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //结算前检查购物车
    public static Observable<TrolleyCheckResponse> checkTrolley(String shoppingCartIds) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shoppingCartIds", shoppingCartIds);
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/shoppingCart/check", map)
                .map(s -> (TrolleyCheckResponse) JsonParse.fromJson(s, TrolleyCheckResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //结算购物车预加载接口
    public static Observable<OrderPreSettleResponse> preSettleTrolley(String shoppingCartIds, String addressId, String deliveryWays) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shoppingCartIds", shoppingCartIds);
        if (addressId != null) map.put("addressId", addressId);
        if (deliveryWays != null) map.put("deliveryWays", deliveryWays);
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/shoppingCart/preSettle", map)
                .map(s -> (OrderPreSettleResponse) JsonParse.fromJson(s, OrderPreSettleResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
