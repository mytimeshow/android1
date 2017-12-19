package cn.czyugang.tcg.client.common;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.entity.Progress;
import cn.czyugang.tcg.client.utils.CLog;
import cn.czyugang.tcg.client.utils.JsonParse;
import cn.czyugang.tcg.client.utils.LogRui;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by wuzihong on 2017/9/11.
 * 网络请求统一入口
 */

public class Network {
    private final String LOG_TAG = "NETWORK";
    private static Network mInstance;
    private OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
    private ApiService mApiService;

    private String mBaseUrl;

    private Network(String baseUrl) {
        mBaseUrl = baseUrl;
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //拿到ApiService
        mApiService = retrofit.create(ApiService.class);
    }

    /**
     * 初始化，创建实例
     *
     * @param url
     * @return
     */
    public static Network init(String url) {
        mInstance = new Network(url);
        return mInstance;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static Network getInstance() {
        return mInstance;
    }

    /**
     * 获取OkHttpClient
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * GET请求
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<String> get(String url, Map<String, Object> params) {
        return get(url, params, null);
    }

    /**
     * GET请求，带Header
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public Observable<String> get(String url, Map<String, Object> params, Map<String, Object> headers) {
        Observable<ResponseBody> observable;
        if (params == null) {
            if (headers == null) {
                observable = mApiService.get(url);
            } else {
                observable = mApiService.getHeader(url, headers);
            }
        } else {
            if (headers == null) {
                observable = mApiService.get(url, params);
            } else {
                observable = mApiService.getHeader(url, params, headers);
            }
        }
        return observable
                .doOnSubscribe(disposable -> CLog.i(LOG_TAG, "--> GET " + getGetUrl(url, params) +
                        "\n--> PARAMS " + params))
                .map(responseBody -> {
                    String bodyStr = responseBody.string();
                    CLog.i(LOG_TAG, "<-- GET " + getGetUrl(url, params) +
                            "\n<-- PARAMS " + params +
                            "\n<-- RESPONSE " + bodyStr);
                    return bodyStr;
                });
    }

    /**
     * POST请求
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<String> post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    /**
     * POST请求，带Header
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public Observable<String> post(String url, Map<String, Object> params, Map<String, Object> headers) {
        Observable<ResponseBody> observable;
        if (params == null) {
            if (headers == null) {
                observable = mApiService.post(url);
            } else {
                observable = mApiService.postHeader(url, headers);
            }
        } else {
            //将参数处理成请求Body
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), JsonParse.toJson(params));
            if (headers == null) {
                observable = mApiService.post(url, body);
            } else {
                observable = mApiService.postHeader(url, body, headers);
            }
        }
        return observable
                .doOnSubscribe(disposable -> CLog.i(LOG_TAG, "--> POST " + getGetUrl(url, params) +
                        "\n--> PARAMS " + params))
                .map(responseBody -> {
                    String bodyStr = responseBody.string();
                    CLog.i(LOG_TAG, "<-- POST " + getGetUrl(url, params) +
                            "\n<-- PARAMS " + params +
                            "\n<-- RESPONSE " + bodyStr);
                    return bodyStr;
                });
    }

    /**
     * 下载文件
     *
     * @param url
     * @param file
     * @return
     */
    public Observable<Progress> download(String url, File file) {
        CompositeDisposable downloadDisposable = new CompositeDisposable();
        Subject<Progress> subject = PublishSubject.<Progress>create().toSerialized();
        return subject
                .doOnSubscribe(disposable -> {
                    mApiService.download(url)
                            .doOnSubscribe(d -> CLog.i(LOG_TAG, "--> DOWNLOAD " + getGetUrl(url, null) +
                                    "\n--> PATH " + file.getPath()))
                            .subscribe(new Observer<ResponseBody>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    downloadDisposable.add(d);
                                }

                                @Override
                                public void onNext(@NonNull ResponseBody responseBody) {
                                    downloadFileSave(responseBody, file, subject);
                                    try {
                                        CLog.i(LOG_TAG, "<-- DOWNLOAD " + getGetUrl(url, null) +
                                                "\n<-- RESPONSE " + responseBody.string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    subject.onError(e);
                                }

                                @Override
                                public void onComplete() {
                                    subject.onComplete();
                                }
                            });
                })
                .doOnDispose(downloadDisposable::dispose);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<Progress> upload(String url, Map<String, Object> params) {
        return upload(url, params, null);
    }

    /**
     * 上传文件,带Header
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<Progress> upload(String url, Map<String, Object> params, Map<String, Object> headers) {
        CompositeDisposable downloadDisposable = new CompositeDisposable();
        Subject<Progress> subject = PublishSubject.<Progress>create().toSerialized();
        return subject
                .doOnSubscribe(disposable -> {
                    Progress progress = new Progress();
                    //将参数处理成请求Body
                    Map<String, RequestBody> body = new HashMap<>();
                    if (params != null && params.size() > 0) {
                        for (Map.Entry<String, Object> entry : params.entrySet()) {
                            Object param = entry.getValue();
                            if (param instanceof File) {
                                //body.put(entry.getKey() + "\";filename=\"" + ((File) param).getName(), createUploadRequestBody((File) param, subject, progress));
                                body.put(entry.getKey(), createUploadRequestBody((File) param, subject, progress));
                            } else if (param instanceof String) {
                                body.put(entry.getKey(), RequestBody.create(MediaType.parse("multipart/form-data"), (String) param));
                            }
                        }
                    }
                    Observable<ResponseBody> observable;
                    if (headers == null) {
                        observable = mApiService.upload(url, body);
                    } else {
                        observable = mApiService.uploadHeader(url, body, headers);
                    }
                    observable
                            .doOnSubscribe(d -> CLog.i(LOG_TAG, "--> UPLOAD " + getGetUrl(url, params) +
                                    "\n--> PARAMS " + params))
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<ResponseBody>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    downloadDisposable.add(d);
                                }

                                @Override
                                public void onNext(@NonNull ResponseBody responseBody) {
                                    try {
                                        String bodyStr = responseBody.string();
                                        progress.setBodyStr(bodyStr);
                                        subject.onNext(progress);
                                        CLog.i(LOG_TAG, "<-- UPLOAD " + getGetUrl(url, params) +
                                                "\n<-- PARAMS " + params +
                                                "\n<-- RESPONSE " + bodyStr);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    subject.onError(e);
                                }

                                @Override
                                public void onComplete() {
                                    subject.onComplete();
                                }
                            });
                })
                .doOnDispose(downloadDisposable::dispose);
    }

    /**
     * 通过下载回调ResponseBody进行文件保存，实现下载进度回调
     *
     * @param responseBody
     * @param file
     * @param subject
     */
    private void downloadFileSave(ResponseBody responseBody, File file, Subject<Progress> subject) {
        InputStream is = responseBody.byteStream();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Progress progress = new Progress();
            progress.setTotal(responseBody.contentLength());//设置总数据量
            long current = 0;
            int len;
            byte[] buf = new byte[2048];
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                current += len;
                progress.setProgress(current);//设置当前下载完成数据量
                subject.onNext(progress);//返回下载进度
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建请求Body，实现上传进度回调
     *
     * @param file
     * @return
     */
    private RequestBody createUploadRequestBody(File file, Subject<Progress> subject, Progress progress) {
        return new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("multipart/form-data");
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
                    progress.setTotal(contentLength());//设置总数据量
                    long current = 0;
                    long len;
                    Buffer buf = new Buffer();
                    while ((len = source.read(buf, 2048)) != -1) {
                        sink.write(buf, len);
                        current += len;
                        progress.setProgress(current);//设置当前下载完成数据量
                        subject.onNext(progress);//返回上传进度
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    /**
     * 将参数拼接到url后面，方便查看以及调用
     *
     * @param url
     * @param params
     * @return
     */
    private String getGetUrl(String url, Map<String, Object> params) {
        url = (url.startsWith("http") || url.startsWith("https")) ? url : mBaseUrl + url;
        if (params != null) {
            boolean hasParam = url.contains("?");//url是否带参数
            StringBuilder urlSb = new StringBuilder(url);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (hasParam) {
                    urlSb.append("&");
                } else {
                    urlSb.append("?");
                    hasParam = true;
                }
                urlSb.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url = urlSb.toString();
        }
        return url;
    }

    private interface ApiService {
        @GET
        Observable<ResponseBody> get(@Url String url);

        @GET
        Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> params);

        @GET
        Observable<ResponseBody> getHeader(@Url String url, @HeaderMap Map<String, Object> headers);

        @GET
        Observable<ResponseBody> getHeader(@Url String url, @QueryMap Map<String, Object> params, @HeaderMap Map<String, Object> headers);

        @POST
        Observable<ResponseBody> post(@Url String url);

        @POST
        Observable<ResponseBody> post(@Url String url, @Body RequestBody params);

        @POST
        Observable<ResponseBody> postHeader(@Url String url, @HeaderMap Map<String, Object> headers);

        @POST
        Observable<ResponseBody> postHeader(@Url String url, @Body RequestBody params, @HeaderMap Map<String, Object> headers);

        @Multipart
        @POST
        Observable<ResponseBody> upload(@Url String url, @PartMap Map<String, RequestBody> params);

        @Multipart
        @POST
        Observable<ResponseBody> uploadHeader(@Url String url, @PartMap Map<String, RequestBody> params, @HeaderMap Map<String, Object> headers);

        @Streaming
        @GET
        Observable<ResponseBody> download(@Url String url);
    }
}
