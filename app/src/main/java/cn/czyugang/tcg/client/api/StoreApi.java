package cn.czyugang.tcg.client.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.GoodCategory;
import cn.czyugang.tcg.client.entity.GoodsResponse;
import cn.czyugang.tcg.client.entity.GoodsSpecResponse;
import cn.czyugang.tcg.client.entity.OrderPreSettleResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.SearchGoodsResponse;
import cn.czyugang.tcg.client.entity.SearchStoreResponse;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.entity.StoreApplyInfo;
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
    public static Observable<GoodsResponse> getGoods(String id, String classifyId, String order,int page,String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId", id);
        map.put("page", page);
        map.put("size", 20);
        if (accessTime!=null) map.put("accessTime",accessTime);
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

    //结算购物车(下单)
    public static Observable<Response<List<String>>> settleTrolley(OrderPreSettleResponse orderPreSettleResponse){
        HashMap<String, Object> map = new HashMap<>();
        List<HashMap<String,String>> deliveryWayList=new ArrayList<>();
        for(OrderPreSettleResponse.StoreMoreInfo storeMoreInfo:orderPreSettleResponse.moreInfoHashMap.values()){
            HashMap<String, String> deliveryMap = new HashMap<>();
            deliveryMap.put("deliveryWay", OrderPreSettleResponse.StoreMoreInfo.transferDeliveryType(storeMoreInfo.selectedDeliveryWay));
            deliveryMap.put("expectDeliveryTime",storeMoreInfo.selectedDeliveryTime);
            deliveryMap.put("note",storeMoreInfo.noteToStore);
            deliveryMap.put("storeId",storeMoreInfo.storeId);
            deliveryWayList.add(deliveryMap);
        }
        List<String> shoppingCartIdList=new ArrayList<>();
        for(TrolleyGoods trolleyGoods:orderPreSettleResponse.trolleyGoodsMap.values()){
            shoppingCartIdList.add(trolleyGoods.trolleyId);
        }
        map.put("addressId",orderPreSettleResponse.address.id);
        map.put("deliveryWayList",deliveryWayList);
        map.put("shoppingCartIdList",shoppingCartIdList);

        return UserOAuth.getInstance()
                .post("api/auth/v1/product/shopping/shoppingCart/settle", map)
                .map(s -> (Response<List<String>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,new JsonParse.Type(List.class,String.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    //api/auth/v1/product/store/get ［可接入］根据id获取店铺商品详情
    public static Observable<Response<Good>> getGoodDetail(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id );
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/store/get", map)
                .map(s -> (Response<Good>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Good.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /*
    *   商家入驻
    * */
    //api/auth/v1/store/apply/submit    [可接入]商家入驻（申请新商家和店铺）
    public static Observable<Response<Object>> storeApply(HashMap<String, Object> map) {
        return UserOAuth.getInstance()
                .post("api/auth/v1/store/apply/submit", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v1/store/apply/status    [可接入]根据用户id查看审核结果/信息
    public static Observable<Response<StoreApplyInfo>> storeApplyStatus() {
        return UserOAuth.getInstance()
                .get("api/auth/v1/store/apply/status",null)
                .map(s -> (Response<StoreApplyInfo>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,StoreApplyInfo.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /*
    *   搜索
    * */
    //api/auth/v1/product/shopping/pre/search/product［DOC-v2］搜索商品预加载
    public static Observable<Response<Object>> searchPre() {
        return UserOAuth.getInstance()
                .get("api/auth/v1/product/shopping/pre/search/product", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    //api/auth/v1/product/shopping/search/product［DOC-v2］搜索商品
    public static Observable<SearchGoodsResponse> searchGoods(HashMap<String, Object> map) {
        //HashMap<String, Object> map = new HashMap<>();
        /*
        *
        *
        *   lat*	number($double)   地理位置维度
        *   lon*	number($double)   地理位置经度
        *
        *   page*	integer($int32)   分页页码
        *   pageSize*	integer($int32)   每页条数
        *   accessToken
        *
        *   title*	string    标题
        *   orderType	string   排序类型(LOCATION-距离最近   GOOD_RATE-好评   PRICE_DESC-价格从低到高   PRICE_ASC-价格从高到低
        *                                   SALE- 销量  不传按发布时间
        *   searchType	string   商品类型( TAKEOUT-外卖; MARKET-商超;不传为全部 )
        *
        *   startPrice	number($float)   价格区间-开始价格
        *   endPrice	number($float)  价格区间-结束价格
        *
        *   serviceTagIds	[string]   description:服务标签ids
        *   tagIds	[string]    description:标签ids
        *   attributeIds	[string]   description:规格属性ids
        *   brandIds	[string]   description:品类id列表
        *   classifyId	string   商品类目id
        *
        *
        * */
        return UserOAuth.getInstance()
                .post("api/auth/v1/product/shopping/search/product", map)
                .map(s -> (SearchGoodsResponse) JsonParse.fromJson(s, SearchGoodsResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v1/product/shopping/search/store［DOC-v2］搜索店铺
    public static Observable<SearchStoreResponse> searchStore(HashMap<String, Object> map) {
        /*
        *   lat*	number($double) 地理位置维度
        *   lon*	number($double) 地理位置经度
        *
        *   page*	integer($int32)  分页页码
        *   pageSize*	integer($int32)  每页条数
        *
        *   title*	string  标题
        *   searchType*	string  商品类型( TAKEOUT-外卖; MARKET-商超 )
        *   orderType*	string  排序类型(LOCATION-距离最近  GOOD_RATE-好评  SALE-好评
        *
        *   classifyId*	string 商品类目id
        *   serviceTagIds*	[string]  description:服务标签ids
        *   tagIds*	[string]  description:标签ids
        *
        * */
        return UserOAuth.getInstance()
                .post("api/auth/v1/product/shopping/search/store", map)
                .map(s -> (SearchStoreResponse) JsonParse.fromJson(s, SearchStoreResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
