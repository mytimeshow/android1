package cn.czyugang.tcg.client.modules.set.presenter;

import android.app.Activity;

import com.tencent.mm.opensdk.modelmsg.SendAuth;

import cn.czyugang.tcg.client.api.TencentApi;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.api.WXApi;
import cn.czyugang.tcg.client.api.WeiboApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.utils.rxbus.UpdateUserInfoEvent;
import cn.czyugang.tcg.client.modules.set.contract.BindThirdpartyContract;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/23.
 */

public class BindThirdpartyPresenter implements BindThirdpartyContract.Presenter {
    private BindThirdpartyContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private UserOAuth mUserOAuth = UserOAuth.getInstance();

    public BindThirdpartyPresenter(BindThirdpartyContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        mView.updateBindInfo(mUserOAuth.getUserInfo());
        RxBus.getObservable(UpdateUserInfoEvent.class)
                .subscribe(updateUserInfoEvent -> {
                    mView.updateBindInfo(updateUserInfoEvent.getUserInfo());
                });
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void bindWX() {
        WXApi wxApi = WXApi.getInstance();
        if (!wxApi.isWXAppInstalled()) {
            mView.showToast("请先安装微信");
            return;
        }
        wxApi.auth()
                .filter(resp -> resp.errCode == 0)
                .flatMap(resp -> wxApi.accessToken(((SendAuth.Resp) resp).code))
                .flatMap(jsonObject -> mUserApi.bindThirdparty(jsonObject.getString("openid"), UserApi.TYPE_THIRDPARTY_WX))
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response response) {
                        switch (response.getCode()) {
                            case 200:
                                UserInfo userInfo = mUserOAuth.getUserInfo();
                                if (userInfo != null) {
                                    userInfo.setIsBindWECHAT("YES");
                                    mUserOAuth.writeUserInfo(userInfo);
                                }
                                break;
                            default:
                                mView.showToast(response.getMessage());
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showError(e);
                        mView.dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingDialog();
                    }
                });
    }

    @Override
    public void bindTencent(Activity activity) {
        TencentApi.getInstance().auth(activity)
                .flatMap(jsonObject -> mUserApi.bindThirdparty(jsonObject.getString("openid"), UserApi.TYPE_THIRDPARTY_TENCENT))
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response response) {
                        switch (response.getCode()) {
                            case 200:
                                UserInfo userInfo = mUserOAuth.getUserInfo();
                                if (userInfo != null) {
                                    userInfo.setIsBindQQ("YES");
                                    mUserOAuth.writeUserInfo(userInfo);
                                }
                                break;
                            default:
                                mView.showToast(response.getMessage());
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showError(e);
                        mView.dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingDialog();
                    }
                });
    }

    @Override
    public void bindWeibo(Activity activity) {
        WeiboApi.getInstance().auth(activity)
                .flatMap(bundle -> mUserApi.bindThirdparty(bundle.getString("uid"), UserApi.TYPE_THIRDPARTY_WEIBO))
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response response) {
                        switch (response.getCode()) {
                            case 200:
                                UserInfo userInfo = mUserOAuth.getUserInfo();
                                if (userInfo != null) {
                                    userInfo.setIsBindWEIBO("YES");
                                    mUserOAuth.writeUserInfo(userInfo);
                                }
                                break;
                            default:
                                mView.showToast(response.getMessage());
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showError(e);
                        mView.dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingDialog();
                    }
                });
    }

    @Override
    public void unbindWX() {
        unbindThirdparty(UserApi.TYPE_THIRDPARTY_WX);
    }

    @Override
    public void unbindTencent() {
        unbindThirdparty(UserApi.TYPE_THIRDPARTY_TENCENT);
    }

    @Override
    public void unbindWeibo() {
        unbindThirdparty(UserApi.TYPE_THIRDPARTY_WEIBO);
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserOAuth.getUserInfo();
    }

    /**
     * 解绑第三方账号
     *
     * @param type
     */
    private void unbindThirdparty(String type) {
        mUserApi.unbindThirdparty(type)
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response response) {
                        switch (response.getCode()) {
                            case 200:
                                UserInfo userInfo = mUserOAuth.getUserInfo();
                                if (userInfo != null) {
                                    switch (type) {
                                        case UserApi.TYPE_THIRDPARTY_WX:
                                            userInfo.setIsBindWECHAT("NO");
                                            break;
                                        case UserApi.TYPE_THIRDPARTY_TENCENT:
                                            userInfo.setIsBindQQ("NO");
                                            break;
                                        case UserApi.TYPE_THIRDPARTY_WEIBO:
                                            userInfo.setIsBindWEIBO("NO");
                                            break;
                                    }
                                    mUserOAuth.writeUserInfo(userInfo);
                                }
                                break;
                            default:
                                mView.showToast(response.getMessage());
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showError(e);
                        mView.dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingDialog();
                    }
                });
    }
}
