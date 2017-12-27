package cn.czyugang.tcg.client.api;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.GroupDetail;
import cn.czyugang.tcg.client.entity.ReduceProduct;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ReduceProductApi {
    //获取降价拍信息
    public static Observable<Response<ReduceProduct>> getReducesProduct(String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityProductId", id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/reduce/detailInfo",map)
                .map(s -> (Response<ReduceProduct>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,ReduceProduct.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //获取团信息
    public static Observable<Response<GroupDetail>> getGroup(String id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityGroupId", id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/marketing/reduce/group/detail",map)
                .map(s -> (Response<GroupDetail>) JsonParse.fromJson(s, new JsonParse.Type(Response.class,GroupDetail.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
