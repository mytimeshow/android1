package cn.czyugang.tcg.client.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.RecordResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/11/21
 */

public class RecordApi {

    //店铺(STORE)/商品(PRODUCT)/资讯(INFO)/任务(TASK)

    //收藏
    public static Observable<Response> collect(String type, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("objectId", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/record/addUserCollection", map)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response> collect(String type, List<String> ids) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("objectIdList", ids);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/record/addUserCollectionBatch", map)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //取消收藏
    public static Observable<Response> deleteCollect(String type, List<String> ids) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("objectId", ids);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/record/delUserCollection", map)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response> deleteCollect(String type, String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        return deleteCollect(type, ids);
    }

    //查询收藏
    public static Observable<RecordResponse> getCollection(String type, int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        if (accessTime != null) map.put("accessTime", accessTime);
        map.put("page", page);
        map.put("size", 20);
        return UserOAuth.getInstance()
                .get("api/auth/v1/user/record/findUserCollection", map)
                .map(s -> (RecordResponse) JsonParse.fromJson(s, RecordResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // api/auth/v1/user/record/addUserFootMark [可接入]记录用户足迹
    public static Observable<RecordResponse> recordFootMark(String type, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("objectId", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/record/addUserFootMark", map)
                .map(s -> (RecordResponse) JsonParse.fromJson(s, RecordResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void recordFootMarkAuto(String type, String id) {
        //店铺(STORE)/商品(PRODUCT)/资讯(INFO)/任务(TASK)
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("objectId", id);
        UserOAuth.getInstance()
                .post("api/auth/v1/user/record/addUserFootMark", map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //api/auth/v1/user/record/delUserFootMark [待测试]删除用户足迹
    public static Observable<Response> deleteFootMark(String type, List<String> ids) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("objectId", ids);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/record/delUserFootMark", map)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v1/user/record/findUserFootMark [待测试]查询用户足迹
    public static Observable<RecordResponse> getFootMark(String type, int page, String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        if (accessTime != null) map.put("accessTime", accessTime);
        map.put("page", page);
        map.put("size", 20);
        return UserOAuth.getInstance()
                .get("api/auth/v1/user/record/findUserFootMark", map)
                .map(s -> (RecordResponse) JsonParse.fromJson(s, RecordResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
