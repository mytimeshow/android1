package cn.czyugang.tcg.client.api;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2017/12/19
 */

public class CommonApi {
    //api/auth/v2/common/search/hot/get/get     [可接入-v2]获取热门搜索
    public static Observable<Response<Object>> getHotSearch() {
        return UserOAuth.getInstance()
                .get("api/auth/v2/common/search/hot/get/get", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
