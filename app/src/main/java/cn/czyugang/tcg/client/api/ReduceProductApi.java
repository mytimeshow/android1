package cn.czyugang.tcg.client.api;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Score;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ReduceProductApi {
    //获取降价拍信息
    public static Observable<Response<Score>> getBaseScore(String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("scoreId", id);
        return UserOAuth.getInstance()
                .get("api/auth/v2/user/bonusPoints/pre",map)
                .map(s -> (Response<Score>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,Score.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
