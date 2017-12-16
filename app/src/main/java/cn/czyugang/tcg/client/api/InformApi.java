package cn.czyugang.tcg.client.api;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;

import cn.czyugang.tcg.client.entity.FollowInformResponse;
import cn.czyugang.tcg.client.entity.MyInformResponse;
import cn.czyugang.tcg.client.entity.NewsInformResponse;
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
    public static Observable<FollowInformResponse> getFollowInform(String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accessTime", "");
        map.put("page", 1);
        map.put("size", 20);
        map.put("type", type);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/follow/list", map)
                .map(s -> (FollowInformResponse) JsonParse.fromJson(s, FollowInformResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //最新资讯
    public static Observable<NewsInformResponse> getNewsInform(String type) {
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
}
