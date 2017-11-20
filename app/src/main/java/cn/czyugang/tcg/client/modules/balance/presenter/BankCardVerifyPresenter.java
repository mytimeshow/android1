package cn.czyugang.tcg.client.modules.balance.presenter;

import android.text.TextUtils;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.contract.BankCardVerifyContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/26.
 */

public class BankCardVerifyPresenter implements BankCardVerifyContract.Presenter {
    private BankCardVerifyContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BalanceApi mBalanceApi = new BalanceApi();

    public BankCardVerifyPresenter(BankCardVerifyContract.View view) {
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
    public void verifyBankInfo(String bankNumber, String name, String idCard, String mobile, boolean agreement) {
        if (TextUtils.isEmpty(name)) {
            mView.showToast("请输入持卡人姓名");
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            mView.showToast("请输入身份证号码");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            mView.showToast("请输入银行预留手机号码");
            return;
        }
        if (!agreement) {
            mView.showToast("请阅读并同意《同城鸽用户协议》");
            return;
        }
        mBalanceApi.verifyBankInfo(bankNumber, name, idCard, mobile)
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
                                mView.startBankMobileVerifyActivity(mobile);
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
