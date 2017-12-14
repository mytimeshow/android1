package cn.czyugang.tcg.client.api;

import java.util.HashMap;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Inform;
import cn.czyugang.tcg.client.entity.MyInform;
import cn.czyugang.tcg.client.entity.MyInformResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/14.
 */

public class InformApi {


    public static Observable<MyInformResponse> getInformByMyself() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accessTime","");
        map.put("page",1);
        map.put("size",20);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/personal/list", map)
                .map(s -> (MyInformResponse) JsonParse.fromJson(s,MyInformResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
