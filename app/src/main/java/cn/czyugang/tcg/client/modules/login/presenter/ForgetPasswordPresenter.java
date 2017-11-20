package cn.czyugang.tcg.client.modules.login.presenter;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.login.contract.ForgetPasswordContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/9/23.
 * 找回密码Presenter
 */

public class ForgetPasswordPresenter implements ForgetPasswordContract.Presenter {
    private final int GOTO_LOGIN_COUNTDOWN = 5;//进入登录页面倒计时
    private ForgetPasswordContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private String mMobile;

    public ForgetPasswordPresenter(ForgetPasswordContract.View view) {
        mView = view;
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
        mUserApi.sendVerificationCode(mobile, UserApi.TYPE_FORGET_PASSWORD)
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
                                mMobile = mobile;
                                mView.gotoInputVerificationCode();
                                mView.updateMobile(mobile);
                                againSendVerificationCodeCountdown();
                                break;
                            case 7000://手机未注册
                                mView.showMobileUnregistered();
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
    public void sendVerificationCode() {
        mUserApi.sendVerificationCode(mMobile, UserApi.TYPE_FORGET_PASSWORD)
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
        mUserApi.setPasswordCheckCode(mMobile, code, UserApi.TYPE_FORGET_PASSWORD)
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
                                mView.gotoSetPassword();
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
    public void setPassword(String password, String againPassword) {
        if (TextUtils.isEmpty(password)) {
            mView.showToast("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(againPassword)) {
            mView.showToast("请再次输入密码");
            return;
        }
        if (!password.equals(againPassword)) {
            mView.showToast("两次输入密码不一致");
            return;
        }
        mUserApi.setPassword(mMobile, password, UserApi.TYPE_FORGET_PASSWORD)
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
                                mView.gotoSetPasswordSucceed();
                                gotoLoginCountdown();
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
     * 进入登录页面倒计时
     */
    private void gotoLoginCountdown() {
        mCompositeDisposable.add(Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(GOTO_LOGIN_COUNTDOWN + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    if (l == GOTO_LOGIN_COUNTDOWN) {
                        mView.finish();
                        return;
                    }
                    mView.updateGotoLoginCountdown((int) (GOTO_LOGIN_COUNTDOWN - l));
                }));
    }
}
