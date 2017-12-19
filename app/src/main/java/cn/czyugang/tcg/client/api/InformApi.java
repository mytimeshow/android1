package cn.czyugang.tcg.client.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.common.UserOAuth;

import cn.czyugang.tcg.client.entity.InformColumn;
import cn.czyugang.tcg.client.entity.InformColumnResponse;
import cn.czyugang.tcg.client.entity.InformFollowResponse;
import cn.czyugang.tcg.client.entity.InformResponse;
import cn.czyugang.tcg.client.entity.MyInformResponse;
import cn.czyugang.tcg.client.entity.NewsInformResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/14.
 */

public class InformApi {


    //个人动态
    public static Observable<MyInformResponse> getInformByMyself() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accessTime", "");
        map.put("page", 1);
        map.put("size", 20);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/personal/list", map)
                .map(s -> (MyInformResponse) JsonParse.fromJson(s, MyInformResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //我的关注
    public static Observable<InformFollowResponse> getFollowInform(String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accessTime", "");
        map.put("page", 1);
        map.put("size", 20);
        map.put("type", type);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/follow/list", map)
                .map(s -> (InformFollowResponse) JsonParse.fromJson(s, InformFollowResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //最新资讯
    public static Observable<NewsInformResponse> getNewsInform() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accessTime", "");
        map.put("page", 1);
        map.put("size", 20);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/new/list", map)
                .map(s -> (NewsInformResponse) JsonParse.fromJson(s, NewsInformResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // 资讯栏目
    public static Observable<InformColumnResponse> getInformColumn(int page){
        HashMap<String, Object> map = new HashMap<>();
        map.put("accessTime", "");
        map.put("page", page);
        map.put("size", 20);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/sort/list", map)
                .map(s -> (InformColumnResponse) JsonParse.fromJson(s, InformColumnResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //关注栏目
    public static Observable<Response> toFollowColumn(String id) {
        Map<String, Object> params = new HashMap<>();
        //栏目id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/info/sort/keep", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //取关栏目
    public static Observable<Response> toUnFollowColumn(String id) {
        Map<String, Object> params = new HashMap<>();
        //栏目id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/info/sort/cancel/keep", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // 根据条件查询资讯
    public static Observable<InformResponse> getInformByCondition(int page,String sortId,String labelId,String publisherId,String keywordType,String keyword) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accessTime", "");
        map.put("page", page);
        map.put("size", 20);
        map.put("sortId", sortId);
        map.put("labelId", labelId);
        map.put("publisherId", publisherId);
        map.put("keywordType", keywordType);
        map.put("keyword", keyword);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/list", map)
                .map(s -> (InformResponse) JsonParse.fromJson(s, InformResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
