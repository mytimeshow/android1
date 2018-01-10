package cn.czyugang.tcg.client.modules.login.presenter;

import android.app.Activity;

import com.tencent.mm.opensdk.modelmsg.SendAuth;

import cn.czyugang.tcg.client.api.TencentApi;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.api.WXApi;
import cn.czyugang.tcg.client.api.WeiboApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserToken;
import cn.czyugang.tcg.client.modules.login.contract.LoginContract;
import cn.czyugang.tcg.client.utils.rxbus.LoginEvent;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/9/5.
 * 登录的Presenter
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private String mAuthInfo;
    private String mAuthToken;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        RxBus.getObservable(LoginEvent.class).subscribe(event -> mView.finish());
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void tencentLogin(Activity activity) {
        TencentApi.getInstance().auth(activity)
                .flatMap(jsonObject -> {
                    mAuthInfo = jsonObject.getString("openid");
                    mAuthToken = jsonObject.getString("access_token");
                    return mUserApi.thirdpartyLogin(mAuthInfo, mAuthToken, UserApi.TYPE_THIRDPARTY_TENCENT);
                })
                .subscribe(new Observer<Response<UserToken>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response<UserToken> response) {
                        switch (response.getCode()) {
                            case 200:
                                UserOAuth.getInstance().login(response);
                                break;
                            case 7004://账号未绑定
                                mView.startBindMobileActivity(UserApi.TYPE_THIRDPARTY_TENCENT, mAuthInfo, mAuthToken);
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
    public void wxLogin() {
        WXApi wxApi = WXApi.getInstance();
        if (!wxApi.isWXAppInstalled()) {
            mView.showToast("请先安装微信");
            return;
        }
        wxApi.auth()
                .filter(resp -> resp.errCode == 0)
                .flatMap(resp -> wxApi.accessToken(((SendAuth.Resp) resp).code))
                .flatMap(jsonObject -> {
                    mAuthInfo = jsonObject.getString("openid");
                    mAuthToken = jsonObject.getString("access_token");
                    return mUserApi.thirdpartyLogin(mAuthInfo, mAuthToken, UserApi.TYPE_THIRDPARTY_WX);
                })
                .subscribe(new Observer<Response<UserToken>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response<UserToken> response) {
                        switch (response.getCode()) {
                            case 200:
                                UserOAuth.getInstance().login(response);
                                break;
                            case 7004://账号未绑定
                                mView.startBindMobileActivity(UserApi.TYPE_THIRDPARTY_WX, mAuthInfo, mAuthToken);
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
    public void weiboLogin(Activity activity) {
        WeiboApi.getInstance().auth(activity)
                .flatMap(bundle -> {
                    mAuthInfo = bundle.getString("uid");
                    mAuthToken = bundle.getString("access_token");
                    return mUserApi.thirdpartyLogin(mAuthInfo, mAuthToken, UserApi.TYPE_THIRDPARTY_WEIBO);
                })
                .subscribe(new Observer<Response<UserToken>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response<UserToken> response) {
                        switch (response.getCode()) {
                            case 200:
                                UserOAuth.getInstance().login(response);
                                break;
                            case 7004://账号未绑定
                                mView.startBindMobileActivity(UserApi.TYPE_THIRDPARTY_WEIBO, mAuthInfo, mAuthToken);
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
