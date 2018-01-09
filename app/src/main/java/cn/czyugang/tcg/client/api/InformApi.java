package cn.czyugang.tcg.client.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.InformColumnResponse;
import cn.czyugang.tcg.client.entity.InformCommentRespone;
import cn.czyugang.tcg.client.entity.InformDetailResponse;
import cn.czyugang.tcg.client.entity.InformFollowResponse;
import cn.czyugang.tcg.client.entity.InformResponse;
import cn.czyugang.tcg.client.entity.MyInformResponse;
import cn.czyugang.tcg.client.entity.NewsInformResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserFansFollow;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/14.
 */

public class InformApi {


    //个人动态
    public static Observable<MyInformResponse> getInformByMyself(String accessTime,int page) {
        HashMap<String, Object> map = new HashMap<>();
        if (accessTime!=null){
            map.put("accessTime", accessTime);
        }
        map.put("page", page);
        map.put("size", 10);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/personal/list", map)
                .map(s -> (MyInformResponse) JsonParse.fromJson(s, MyInformResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //我的关注
    public static Observable<InformFollowResponse> getFollowInform(String type,int page,String accessTime) {
        HashMap<String, Object> map = new HashMap<>();
        if (accessTime!=null){
            map.put("accessTime", accessTime);
        }
        map.put("page", page);
        map.put("size", 10);
        map.put("type", type);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/follow/list", map)
                .map(s -> (InformFollowResponse) JsonParse.fromJson(s, InformFollowResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //最新资讯
    public static Observable<NewsInformResponse> getNewsInform(String accessTime,int page) {
        HashMap<String, Object> map = new HashMap<>();
        if (accessTime!=null){
            map.put("accessTime", accessTime);
        }
        map.put("page", page);
        map.put("size", 10);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/new/list", map)
                .map(s -> (NewsInformResponse) JsonParse.fromJson(s, NewsInformResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // 资讯栏目
    public static Observable<InformColumnResponse> getInformColumn(String accessTime,int page){
        HashMap<String, Object> map = new HashMap<>();
        if (accessTime!=null){
            map.put("accessTime", accessTime);
        }
        map.put("page", page);
        map.put("size", 10);
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
        map.put("size", 10);
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

    //用户粉丝
    public static Observable<Response<List<UserFansFollow>>> userFansList(String userId, int page) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("targetUserId", userId);
        params.put("accessTime", "");
        params.put("page", page);
        params.put("size", 10);
        return UserOAuth.getInstance()
                .get("api/auth/v1/user/media/relation/list/fans", params)
                .map(s -> (Response<List<UserFansFollow>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, UserFansFollow.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //用户关注
    public static Observable<Response<List<UserFansFollow>>> userFollowList(String userId, int page) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("followUserId", userId);
        params.put("accessTime", "");
        params.put("page", page);
        params.put("size", 10);
        return UserOAuth.getInstance()
                .get("api/auth/v1/user/media/relation/list/follows", params)
                .map(s -> (Response<List<UserFansFollow>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, UserFansFollow.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //关注用户/媒体
    public static Observable<Response> toFollowUser(String id) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/media/relation/follow", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //取关用户/媒体
    public static Observable<Response> toUnFollowUser(String id) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/media/relation/cancel/follow", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //点赞资讯
    public static Observable<Response> toLikeInform(String id) {
        Map<String, Object> params = new HashMap<>();
        //资讯id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/info/like", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //取消点赞资讯
    public static Observable<Response> toUnLikeInform(String id) {
        Map<String, Object> params = new HashMap<>();
        //资讯id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/info/cancel/like", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v1/info/get [可接入]根据id获取资讯详情
    public static Observable<InformDetailResponse> getInformDetail(String id){
        RecordApi.recordFootMarkAuto("INFO",id);
        HashMap<String,Object> map=new HashMap<>();
        map.put("id",id);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/get",map)
                .map(s -> (InformDetailResponse) JsonParse.fromJson(s,InformDetailResponse.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    ///api/auth/v1/info/follow/list/pre[可接入]我的关注预备数据
    public static Observable<Response> getInformLabel(){
        HashMap<String,Object> map=new HashMap<>();
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/follow/list/pre",map)
                .map(s -> (Response) JsonParse.fromJson(s,Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //api/auth/v1/info/comment/hot/top5[可接入]查看资讯热评前5评论列表
    public static Observable<InformCommentRespone> getHotComment(String infoId){
        HashMap<String,Object> map=new HashMap<>();
        map.put("infoId",infoId);
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/comment/hot/top5",map)
                .map(s -> (InformCommentRespone) JsonParse.fromJson(s,InformCommentRespone.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v1/info/comment/list[可接入]查看资讯最新评论列表
    public static Observable<InformCommentRespone> getNewComment(String infoId,int page,String accessTime){
        HashMap<String,Object> map=new HashMap<>();
        map.put("infoId",infoId);
        map.put("size",20);
        if (accessTime!=null) {
            map.put("accessTime",accessTime);
            map.put("page",page);
        }else {
            map.put("page",1);
        }
        return UserOAuth.getInstance()
                .get("api/auth/v1/info/comment/list",map)
                .map(s -> (InformCommentRespone) JsonParse.fromJson(s,InformCommentRespone.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //点赞评论
    public static Observable<Response> toLikeComment(String id) {
        Map<String, Object> params = new HashMap<>();
        //评论id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/info/comment/like", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //取消点赞评论
    public static Observable<Response> toUnLikeComment(String id) {
        Map<String, Object> params = new HashMap<>();
        //评论id
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/info/comment/cancel/like", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
