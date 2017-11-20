package cn.czyugang.tcg.client.modules.balance.presenter;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.contract.AddBankCardPasswordContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/26.
 */

public class AddBankCardPasswordPresenter implements AddBankCardPasswordContract.Presenter {
    private AddBankCardPasswordContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private BalanceApi mBalanceApi = new BalanceApi();

    private BankCardInfo mBankCardInfo;

    public AddBankCardPasswordPresenter(AddBankCardPasswordContract.View view, BankCardInfo bankCardInfo) {
        mView = view;
        mBankCardInfo = bankCardInfo;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void verifyPayPassword(String password) {
        mUserApi.verifyPayPassword(password)
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
                                mView.startAddBankCardActivity(password);
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
                        mView.clearPassword();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingDialog();
                        mView.clearPassword();
                    }
                });
    }

    @Override
    public void unbindBanKCard(String password) {
        String bankCardId = null;
        if (mBankCardInfo != null) {
            BankCard bankCard = mBankCardInfo.getBankCard();
            bankCardId = bankCard != null ? bankCard.getId() : null;
        }
        mBalanceApi.unbindBanKCard(bankCardId, password)
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
                        mView.clearPassword();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingDialog();
                        mView.clearPassword();
                    }
                });
    }
}
