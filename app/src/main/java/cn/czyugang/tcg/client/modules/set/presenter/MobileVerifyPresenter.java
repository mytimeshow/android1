package cn.czyugang.tcg.client.modules.set.presenter;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.activity.MobileVerifyActivity;
import cn.czyugang.tcg.client.modules.set.contract.MobileVerifyContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/21.
 */

public class MobileVerifyPresenter implements MobileVerifyContract.Presenter {
    private MobileVerifyContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private int mType;
    private String mMobile;

    public MobileVerifyPresenter(MobileVerifyContract.View view, int type, String mobile) {
        mView = view;
        mType = type;
        mMobile = mobile;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void sendVerificationCode() {
        String type = null;
        switch (mType) {
            case MobileVerifyActivity.TYPE_SET_PASSWORD:
                type = UserApi.TYPE_RESET_PASSWORD;
                break;
            case MobileVerifyActivity.TYPE_SET_PAY_PASSWORD:
            case MobileVerifyActivity.TYPE_BIND_BANK_CARD:
                type = UserApi.TYPE_RESET_PAY_PASSWORD;
                break;
            case MobileVerifyActivity.TYPE_FORGET_PAY_PASSWORD:
                type = UserApi.TYPE_FORGET_PAY_PASSWORD;
                break;
            case MobileVerifyActivity.TYPE_BIND_EMAIL:
                type = UserApi.TYPE_BIND_EMAIL;
                break;
            case MobileVerifyActivity.TYPE_UNBIND_EMAIL:
                type = UserApi.TYPE_UNBIND_EMAIL;
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
            case MobileVerifyActivity.TYPE_SET_PASSWORD:
                setPasswordCheckCode(code);
                break;
            case MobileVerifyActivity.TYPE_SET_PAY_PASSWORD:
            case MobileVerifyActivity.TYPE_BIND_BANK_CARD:
                setPayPasswordCheckCode(code);
                break;
            case MobileVerifyActivity.TYPE_FORGET_PAY_PASSWORD:
                forgetPayPasswordCheckCode(code);
                break;
            case MobileVerifyActivity.TYPE_BIND_EMAIL:
                bindEmailCheckCode(code);
                break;
            case MobileVerifyActivity.TYPE_UNBIND_EMAIL:
                unbindEmail(code);
                break;
        }
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
     * 检查设置密码的验证码
     *
     * @param code
     */
    private void setPasswordCheckCode(String code) {
        mUserApi.setPasswordCheckCode(mMobile, code, UserApi.TYPE_RESET_PASSWORD)
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
                                mView.startSetPasswordActivity();
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
     * 检查设置支付密码的验证码
     *
     * @param code
     */
    private void setPayPasswordCheckCode(String code) {
        mUserApi.setPayPasswordCheckCode(mMobile, code, UserApi.TYPE_RESET_PAY_PASSWORD)
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
                                mView.startSetPayPasswordActivity();
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
     * 检查找回支付密码的验证码
     *
     * @param code
     */
    private void forgetPayPasswordCheckCode(String code) {
        mUserApi.setPayPasswordCheckCode(mMobile, code, UserApi.TYPE_FORGET_PAY_PASSWORD)
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
                                mView.startForgetPayPasswordActivity();
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
     * 检查绑定邮箱的验证码
     *
     * @param code
     */
    private void bindEmailCheckCode(String code) {
        mUserApi.bindEmailCheckCode(mMobile, code)
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
                                mView.startBindEmailActivity();
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
     * 邮箱解绑
     *
     * @param code
     */
    private void unbindEmail(String code) {
        mUserApi.unbindEmail(mMobile, code)
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
                                        userBase.setEmail("");
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
