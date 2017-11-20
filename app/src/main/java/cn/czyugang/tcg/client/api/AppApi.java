package cn.czyugang.tcg.client.api;

import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Progress;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UploadFile;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuzihong on 2017/10/24.
 */

public class AppApi {
    public static final String TYPE_AVATAR = "FACE";

    /**
     * 上传文件，返回进度
     *
     * @param file
     * @param type
     * @return
     */
    public Observable<Progress> uploadFileProgress(File file, String type) {
        Map<String, Object> params = new HashMap<>();
        //文件
        params.put("file", file);
        //业务类型
        params.put("type", type);
        return UserOAuth.getInstance()
                .upload("/api/auth/v1/file/uploadFile", params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 上传文件
     *
     * @param file
     * @param type
     * @return
     */
    public Observable<Response<UploadFile>> uploadFile(File file, String type) {
        return uploadFileProgress(file, type)
                .filter(progress -> !TextUtils.isEmpty(progress.getBodyStr()))
                .map(progress -> (Response<UploadFile>) JsonParse.fromJson(progress.getBodyStr(), new JsonParse.Type(Response.class, UploadFile.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
