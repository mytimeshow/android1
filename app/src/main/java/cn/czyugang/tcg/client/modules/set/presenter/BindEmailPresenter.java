package cn.czyugang.tcg.client.modules.set.presenter;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.contract.BindEmailContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/23.
 */

public class BindEmailPresenter implements BindEmailContract.Presenter {
    private BindEmailContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();

    public BindEmailPresenter(BindEmailContract.View view) {
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
    public void sendVerificationCode(String email) {
        if (TextUtils.isEmpty(email)) {
            mView.showToast("请输入邮箱账号");
            return;
        }
        mUserApi.sendEmailVerificationCode(email)
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
    public void bindEmail(String email, String code) {
        if (TextUtils.isEmpty(email)) {
            mView.showToast("请输入邮箱账号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            mView.showToast("请输入邮箱验证码");
            return;
        }
        mUserApi.bindEmail(email, code)
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
                                        userBase.setEmail(email);
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

    /**
     * 开始再次发送验证码倒计时
     */
    private void againSendVerificationCodeCountdown() {
        mCompositeDisposable.add(Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(Config.VERIFICATION_CODE_SEND_INTERVAL + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> mView.updateVerificationCodeCountdown((int) (Config.VERIFICATION_CODE_SEND_INTERVAL - l))));
    }
}
