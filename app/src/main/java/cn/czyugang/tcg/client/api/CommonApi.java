package cn.czyugang.tcg.client.api;

import java.util.HashMap;

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

    //api/auth/v1/common/qr/scan [Doc]扫描店铺/商品/活动二维码
    public static Observable<Response<Object>> scanResult(String identifier) {
        //二维码标识符,格式type-id(如product-1321321)
        HashMap<String, Object> map = new HashMap<>();
        map.put("identifier ",identifier );
        return UserOAuth.getInstance()
                .get("api/auth/v1/common/qr/scan", null)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
