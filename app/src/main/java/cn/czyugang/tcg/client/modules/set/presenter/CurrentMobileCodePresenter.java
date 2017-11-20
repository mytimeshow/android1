package cn.czyugang.tcg.client.modules.set.presenter;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.set.contract.CurrentMobileCodeContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/20.
 */

public class CurrentMobileCodePresenter implements CurrentMobileCodeContract.Presenter {
    private CurrentMobileCodeContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private String mMobile;

    public CurrentMobileCodePresenter(CurrentMobileCodeContract.View view, String mobile) {
        mView = view;
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
        mUserApi.sendVerificationCode(mMobile, UserApi.TYPE_BIND_MOBILE1)
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
        mUserApi.modifyMobileCheckCode(mMobile, code)
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
                                mView.startBindMobileActivity();
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
