package cn.czyugang.tcg.client.api;


import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Score;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class ScoreApi {

    //获取基本的积分信息
    public static Observable<Response<Score>> getBaseScore(String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("scoreId", id);
        map.put("page",10);
        return UserOAuth.getInstance()
                .get("api/auth/v2/user/bonusPoints/pre",map)
                .map(s -> (Response<Score>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Score.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
//获取积分明细新信息
    public  static Observable<Response<List<Score>>> getScoreDetail(String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("scoreId", id);
        map.put("page",10);
        map.put("size",120);
        return UserOAuth.getInstance()
                .get("api/auth/v2/user/bonusPoints/query/record/dict", map)
                .map(s -> (Response<List<Score>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,new JsonParse.Type(List.class,Score.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());



    }





    //对服务器信息进行更新
    public static Observable<Response<Object>> PostUpdataScore(HashMap<String, Object> map){
        return UserOAuth.getInstance()
                .post("api/auth/v2/user/bonusPoints/add", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
