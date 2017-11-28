package cn.czyugang.tcg.client.api;

import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Collection;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class RecordApi {

    //收藏
    private static Observable<Response> collect(String type,String id){
        HashMap<String,Object> map=new HashMap<>();
        map.put("objectId",id);
        map.put("type",type);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/record/addUserCollection",map)
                .map(s -> (Response) JsonParse.fromJson(s,Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response> collectStore(String id){
        return collect("STORE",id);
    }

    public static Observable<Response> collectGoods(String id){
        return collect("PRODUCT",id);
    }

    public static Observable<Response> collectInfo(String id){
        return collect("INFO",id);
    }

    public static Observable<Response> collectTask(String id){
        return collect("TASK",id);
    }

    //取消收藏
    private static Observable<Response> deleteCollect(String type,String... ids){
        HashMap<String,Object> map=new HashMap<>();
        map.put("type",type);
        map.put("objectId",ids);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/record/delUserCollection",map)
                .map(s -> (Response) JsonParse.fromJson(s,Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response> deleteCollectStore(String... ids){
        return deleteCollect("STORE",ids);
    }

    public static Observable<Response> deleteCollectGoods(String... ids){
        return deleteCollect("PRODUCT",ids);
    }

    public static Observable<Response> deleteCollectInfo(String... ids){
        return deleteCollect("INFO",ids);
    }

    public static Observable<Response> deleteCollectTask(String... ids){
        return deleteCollect("TASK",ids);
    }

    //查询收藏
    private static Observable<Response<List<Collection>>> getCollection(String type){
        HashMap<String,Object> map=new HashMap<>();
        map.put("type",type);
        return UserOAuth.getInstance()
                .get("api/auth/v1/user/record/findUserCollection",map)
                .map(s -> (Response<List<Collection>>) JsonParse.fromJson(s,new JsonParse.Type(Response.class,new JsonParse.Type(List.class,Collection.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response<List<Collection>>> getCollectionStore(){
        return getCollection("STORE");
    }

    public static Observable<Response<List<Collection>>> getCollectionGood(){
        return getCollection("PRODUCT");
    }

    public static Observable<Response<List<Collection>>> getCollectionInfo(){
        return getCollection("INFO");
    }

    public static Observable<Response<List<Collection>>> getCollectionTAsk(){
        return getCollection("STORE");
    }
}
