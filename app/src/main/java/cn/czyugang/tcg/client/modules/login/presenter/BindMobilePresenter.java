package cn.czyugang.tcg.client.modules.login.presenter;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.entity.UserToken;
import cn.czyugang.tcg.client.modules.login.activity.BindMobileActivity;
import cn.czyugang.tcg.client.modules.login.contract.BindMobileContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/9/23.
 * 绑定手机的Presenter
 */

public class BindMobilePresenter implements BindMobileContract.Presenter {
    private BindMobileContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private int mType;
    private String mAuthType;
    private String mAuthInfo;
    private String mAuthToken;
    private String mMobile;

    public BindMobilePresenter(BindMobileContract.View view, int type, String authType, String authInfo, String authToken) {
        mView = view;
        mType = type;
        mAuthType = authType;
        mAuthInfo = authInfo;
        mAuthToken = authToken;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void sendVerificationCode(String mobile, boolean agreement) {
        if (mobile.length() != 11) {
            mView.showToast("请输入正确的11位手机号码");
            return;
        }
        if (!agreement) {
            mView.showToast("请阅读并同意《同城鸽用户协议》");
            return;
        }
        switch (mType) {
            case BindMobileActivity.TYPE_BIND_MOBILE:
                checkMobileBindThirdparty(mobile);
                break;
            case BindMobileActivity.TYPE_MODIFY_MOBILE:
                sendVerificationCode(mobile);
                break;
        }

    }

    @Override
    public void sendVerificationCode() {
        String type = null;
        switch (mType) {
            case BindMobileActivity.TYPE_BIND_MOBILE:
                type = UserApi.TYPE_BIND_MOBILE;
                break;
            case BindMobileActivity.TYPE_MODIFY_MOBILE:
                type = UserApi.TYPE_BIND_MOBILE2;
                break;
        }
        mUserApi.sendVerificationCode(mMobile, type)
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
                                againSendVerificationCodeCountdown();
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
    public void commitVerificationCode(String code) {
        if (code.length() != 6) {
            mView.showToast("请输入正确的6位验证码");
            return;
        }
        switch (mType) {
            case BindMobileActivity.TYPE_BIND_MOBILE:
                thirdpartyBindMobile(code);
                break;
            case BindMobileActivity.TYPE_MODIFY_MOBILE:
                modifyMobile(code);
                break;
        }
    }

    /**
     * 发送验证码
     *
     * @param mobile
     */
    private void sendVerificationCode(String mobile) {
        String type = null;
        switch (mType) {
            case BindMobileActivity.TYPE_BIND_MOBILE:
                type = UserApi.TYPE_BIND_MOBILE;
                break;
            case BindMobileActivity.TYPE_MODIFY_MOBILE:
                type = UserApi.TYPE_BIND_MOBILE2;
                break;
        }
        mUserApi.sendVerificationCode(mobile, type)
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Response response) {
                        switch (response.getCode()) {
                            case 200:
                                mMobile = mobile;
                                mView.gotoInputVerificationCode();
                                mView.updateMobile(mobile);
                                againSendVerificationCodeCountdown();
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

    /**
     * 开始再次发送验证码倒计时
     */
    private void againSendVerificationCodeCountdown() {
        mCompositeDisposable.add(Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(Config.VERIFICATION_CODE_SEND_INTERVAL + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> mView.updateVerificationCodeCountdown((int) (Config.VERIFICATION_CODE_SEND_INTERVAL - l))));
    }

    /**
     * 检查第三方账号是否被其他手机号绑定
     *
     * @param mobile
     */
    private void checkMobileBindThirdparty(String mobile) {
        mUserApi.checkMobileBindThirdparty(mobile, mAuthType)
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
                                sendVerificationCode(mobile);
                                break;
                            case 7030://手机被绑定
                                mView.showMobileBinding();
                                mView.dismissLoadingDialog();
                                break;
                            default:
                                mView.showToast(response.getMessage());
                                mView.dismissLoadingDialog();
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
                    }
                });
    }

    /**
     * 绑定第三方账号
     *
     * @param code
     */
    private void thirdpartyBindMobile(String code) {
        mUserApi.thirdpartyBindMobile(mAuthInfo, mAuthToken, mMobile, code, mAuthType)
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
                                mView.finish();
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

    /**
     * 修改绑定的手机号码
     *
     * @param code
     */
    private void modifyMobile(String code) {
        mUserApi.modifyMobile(mMobile, code)
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
                                UserOAuth userOAuth = UserOAuth.getInstance();
                                UserInfo userInfo = userOAuth.getUserInfo();
                                if (userInfo != null) {
                                    UserBase userBase = userInfo.getUserBase();
                                    if (userBase != null) {
                                        userBase.setPhone(mMobile);
                                        userOAuth.writeUserInfo(userInfo);
                                    }
                                }
                                mView.finish();
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
