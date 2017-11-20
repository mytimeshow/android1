package cn.czyugang.tcg.client.modules.balance.presenter;

import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.contract.BankMobileVerifyContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/30.
 */

public class BankMobileVerifyPresenter implements BankMobileVerifyContract.Presenter {
    private BankMobileVerifyContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private BalanceApi mBalanceApi = new BalanceApi();
    private String mMobile;
    private String mPayPassword;

    public BankMobileVerifyPresenter(BankMobileVerifyContract.View view, String mobile, String payPassword) {
        mView = view;
        mMobile = mobile;
        mPayPassword = payPassword;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void sendVerificationCode() {
        mUserApi.sendVerificationCode(mMobile, UserApi.TYPE_BIND_BANK_CARD)
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
    public void commitVerificationCode(String code, String bank, String bankNumber, String bankType, String name, String idCard) {
        if (code.length() != 6) {
            mView.showToast("请输入正确的6位验证码");
            return;
        }
        mBalanceApi.verifyBankMobile(mMobile, code, bank, bankNumber, bankType, name, idCard, mPayPassword)
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
                                if ("YES".equals(DictUtil.getObject(response.getValues(), "isSetPayPassword"))) {
                                    mView.finish();
                                    return;
                                }
                                if ("YES".equals(DictUtil.getObject(response.getValues(), "needCheckPhone"))) {
                                    mView.startMobileVerifyActivity(DictUtil.getObject(response.getValues(), "userPhone"));
                                } else {
                                    mView.startSetPayPasswordActivity();
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
