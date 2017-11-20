package cn.czyugang.tcg.client.modules.balance.presenter;

import android.text.TextUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.BankCardStyle;
import cn.czyugang.tcg.client.entity.RealNameAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.contract.AddBankCardContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/25.
 */

public class AddBankCardPresenter implements AddBankCardContract.Presenter {
    private AddBankCardContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BalanceApi mBalanceApi = new BalanceApi();

    private Disposable mVerifyBankNumberDisposable;
    private String mErrMessage;
    private boolean mLoading = false;
    private boolean mGotoNext = false;
    private BankCardInfo mBankCardInfo;
    private RealNameAuth mRealNameAuth;

    public AddBankCardPresenter(AddBankCardContract.View view) {
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
    public void verifyBankNumber(String number) {
        mBalanceApi.verifyBankNumber(number)
                .subscribe(new Observer<Response<BankCard>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mVerifyBankNumberDisposable = d;
                        mLoading = true;
                    }

                    @Override
                    public void onNext(@NonNull Response<BankCard> response) {
                        switch (response.getCode()) {
                            case 200:
                                mErrMessage = null;
                                mBankCardInfo = new BankCardInfo();
                                BankCard bankCard = response.getData();
                                if (bankCard != null) {
                                    bankCard.setCardNum(number);
                                }
                                mBankCardInfo.setBankCard(bankCard);
                                mBankCardInfo.setBankCardStyle(DictUtil.getObject(response.getValues(), "bankStyle", BankCardStyle.class));
                                mRealNameAuth = DictUtil.getObject(response.getValues(), "authInfo", RealNameAuth.class);
                                if (mGotoNext) {
                                    mView.startBankCardVerifyActivity(mBankCardInfo, mRealNameAuth);
                                } else {
                                    mView.updateBankCardInfo(mBankCardInfo);
                                }
                                break;
                            default:
                                if (mGotoNext) {
                                    mView.showToast(response.getMessage());
                                } else {
                                    mErrMessage = response.getMessage();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (mGotoNext) {
                            mGotoNext = false;
                            mView.showError(e);
                            mView.dismissLoadingDialog();
                        } else {
                            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                                mErrMessage = "网络不稳定";
                            } else if (e instanceof UserOAuth.LoginExpiredException) {
                                mErrMessage = "登录过期,请重新登录";
                            } else {
                                mErrMessage = "服务器异常";
                            }
                            e.printStackTrace();
                        }
                        mLoading = false;
                    }

                    @Override
                    public void onComplete() {
                        mLoading = false;
                        if (mGotoNext) {
                            mGotoNext = false;
                            mView.dismissLoadingDialog();
                        }
                    }
                });
    }

    @Override
    public void disposeVerifyBankNumber() {
        if (mVerifyBankNumberDisposable != null && !mVerifyBankNumberDisposable.isDisposed()) {
            mVerifyBankNumberDisposable.dispose();
        }
    }

    @Override
    public void gotoNext(String number) {
        if (number.length() != 16 && number.length() != 19) {
            mView.showToast("请输入正确的银行卡号");
            return;
        }
        if (mLoading) {
            mGotoNext = true;
            mView.showLoadingDialog();
            return;
        }
        if (!TextUtils.isEmpty(mErrMessage)) {
            mView.showToast(mErrMessage);
            return;
        }
        mView.startBankCardVerifyActivity(mBankCardInfo, mRealNameAuth);
    }
}
