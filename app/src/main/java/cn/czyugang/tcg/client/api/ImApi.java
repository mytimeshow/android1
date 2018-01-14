package cn.czyugang.tcg.client.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.ImUser;
import cn.czyugang.tcg.client.entity.ImMsg;
import cn.czyugang.tcg.client.entity.ImSendMsg;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ruiaa
 * @date 2018/1/11
 */

public class ImApi {

    //api/auth/v3/im/user/info [doc-v3]获取用户信息
    public static Observable<Response<ImUser>> userInfo(String account) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/user/info", map)
                .map(s -> (Response<ImUser>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, ImUser.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v3/im/user/session [doc-v3]获取聊天会话
    public static Observable<Response<List<ImUser>>> getSessions(String account) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", account);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/user/session ", map)
                .map(s -> (Response<List<ImUser>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class,ImUser.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //api/auth/v3/im/delete/session [doc-v3]删除用户会话
    public static Observable<Response<Object>> deleteSessions(String imAccount,String deleteAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("imAccount", imAccount);
        map.put("deleteAccount",deleteAccount);
        return UserOAuth.getInstance()
                .post("api/auth/v2/order/product/userCancel", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v3/im/block/all [doc-v3]获取所有屏蔽用户         不再接收新消息
    public static Observable<Response<List<ImUser>>> getBlock(String account) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/block/all", map)
                .map(s -> (Response<List<ImUser>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class,ImUser.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //api/auth/v3/im/block/user [doc-v3]屏蔽用户
    public static Observable<Response<Object>> addBlock(String imAccount,String blockAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("imAccount", imAccount);
        map.put("blockAccount",blockAccount);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/block/user", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //api/auth/v3/im/open/user [doc-v3]打开屏蔽用户  ? 取消屏蔽
    public static Observable<Response<Object>> deleteBlock(String imAccount,String blockAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("imAccount", imAccount);
        map.put("blockAccount",blockAccount);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/open/user", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //api/auth/v3/im/user/unnoticed/all [doc-v3]获取所有勿扰用户        用户在当前里面则不通知、但有新消息接收
    public static Observable<Response<List<String>>> getUnnotice(String account) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/user/unnoticed/all", map)
                .map(s -> (Response<List<String>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class,String.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    //api/auth/v3/im/open/user/unnoticed [doc-v3]删除勿扰用户
    public static Observable<Response<Object>> deleteUnnotice(String imAccount,String unnoticeImAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("imAccount", imAccount);
        map.put("unnoticeImAccount",unnoticeImAccount);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/open/user/unnoticed", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    //api/auth/v3/im/user/unnoticed [doc-v3]添加勿扰用户
    public static Observable<Response<Object>> addUnnotice(String imAccount,String unnoticeImAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("imAccount", imAccount);
        map.put("unnoticeImAccount",unnoticeImAccount);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/user/unnoticed", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v3/im/get/online/service [doc-v3]获取当前在线客服
    public static Observable<Response<List<ImUser>>> getCustomerService() {
        return UserOAuth.getInstance()
                .get("api/auth/v3/im/get/online/service", null)
                .map(s -> (Response<List<ImUser>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class,ImUser.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v3/im/history [doc-v3]获取聊天记录
    public static Observable<Response<List<ImMsg>>> history(String sendAccount, String targetAccount, String keyword, String messageType,
                                                            String accessTime, int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sendAccount",sendAccount);
        map.put("targetAccount",targetAccount);
        if (keyword!=null&&!keyword.isEmpty()) map.put("keyword",keyword);
        //TEXT 文字消息、 IMG 图片消息、 AUDIO 语音消息、 PRODUCT 商品详情、 ACTIVITY 活动 、 INFO 资讯
        if (messageType!=null&&!messageType.isEmpty()) map.put("messageType",messageType);
        if (accessTime!=null){
            map.put("messageType",messageType);
            map.put("page",page);
        }else {
            map.put("page",1);
        }
        map.put("size",20);
        return UserOAuth.getInstance()
                .get("api/auth/v3/im/history", map)
                .map(s -> (Response<List<ImMsg>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, ImMsg.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //api/auth/v3/im/sendAccount/text [doc-v3]发送文字消息
    public static Observable<Response<Object>> sendText(ImSendMsg imSendMsg) {
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/sendAccount/text", imSendMsg.toMap())
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    //api/auth/v3/im/sendAccount/image [doc-v3]发送图片消息
    public static Observable<Response<Object>> sendImg(String file ,String sendAccount,String targetAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("file",file);
        map.put("send",sendAccount);
        List<String> targets=new ArrayList<>();
        targets.add(targetAccount);
        map.put("targets",targets);
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/sendAccount/image", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    //api/auth/v3/bus/im/sendAccount/sound [doc-v3]发送语音消息
    public static Observable<Response<Object>> sendSound(String file ,String sendAccount,String targetAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("file",file);
        map.put("send",sendAccount);
        List<String> targets=new ArrayList<>();
        targets.add(targetAccount);
        map.put("targets",targets);
        return UserOAuth.getInstance()
                .post("api/auth/v3/bus/im/sendAccount/sound", map)
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    //api/auth/v3/im/sendAccount/ext [doc-v3]发送扩展消息
    public static Observable<Response<Object>> sendExt(ImSendMsg imSendMsg) {
        return UserOAuth.getInstance()
                .post("api/auth/v3/im/sendAccount/ext", imSendMsg.toMap())
                .map(s -> (Response<Object>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Object.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    //api/auth/v3/bus/im/file?fileId [doc-v3]获取im文件
}
