package cn.czyugang.tcg.client.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.ImNoticeMsg;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/29.
 */

public class ImApi {

    //api/auth/v1/bus/message/getByCategoryPage[可接入-v2]根据消息分类分页获取消息
    public static Observable<Response<List<ImNoticeMsg>>> getNoticeList(String id, int page) {
        Map<String, Object> params = new HashMap<>();
        //消息分类id
        params.put("categoryId ", id);
        params.put("accessTime", "");
        params.put("page", page);
        params.put("size", 10);
        return UserOAuth.getInstance()
                .get("api/auth/v1/bus/message/getByCategoryPage", params)
                .map(s -> (Response<List<ImNoticeMsg>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, ImNoticeMsg.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
