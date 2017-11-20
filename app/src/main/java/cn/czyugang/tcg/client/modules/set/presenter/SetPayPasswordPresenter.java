package cn.czyugang.tcg.client.modules.set.presenter;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.RealNameAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.activity.SetPayPasswordActivity;
import cn.czyugang.tcg.client.modules.set.contract.SetPayPasswordContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/21.
 */

public class SetPayPasswordPresenter implements SetPayPasswordContract.Presenter {
    private SetPayPasswordContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private BalanceApi mBalanceApi = new BalanceApi();

    private int mType;

    public SetPayPasswordPresenter(SetPayPasswordContract.View view, int type) {
        mView = view;
        mType = type;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void setPayPassword(String password, String againPassword) {
        if (!password.equals(againPassword)) {
            mView.showToast("两次输入密码不一致");
            mView.clearPassword();
            return;
        }
        String type = null;
        switch (mType) {
            case SetPayPasswordActivity.TYPE_SET_PAY_PASSWORD:
                type = UserApi.TYPE_RESET_PAY_PASSWORD;
                break;
            case SetPayPasswordActivity.TYPE_FORGET_PAY_PASSWORD:
                type = UserApi.TYPE_FORGET_PAY_PASSWORD;
                break;
        }
        mUserApi.setPayPassword(password, type)
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
                                if (mType == SetPayPasswordActivity.TYPE_SET_PAY_PASSWORD) {
                                    UserOAuth userOAuth = UserOAuth.getInstance();
                                    UserInfo userInfo = userOAuth.getUserInfo();
                                    if (userInfo != null) {
                                        UserDetail userDetail = userInfo.getUserDetail();
                                        if (userDetail != null) {
                                            userDetail.setSetPayPassword("YES");
                                            userOAuth.writeUserInfo(userInfo);
                                        }
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
    public void bindBankCard(String password, String againPassword, BankCardInfo bankCardInfo, RealNameAuth realNameAuth, String bankMobile) {
        if (!password.equals(againPassword)) {
            mView.showToast("两次输入密码不一致");
            mView.clearPassword();
            return;
        }
        BankCard bankCard = bankCardInfo.getBankCard();
        mBalanceApi.bindBankCard(bankMobile, bankCard.getOwnedBank(), bankCard.getCardNum(), bankCard.getCardType(), realNameAuth.getName(), realNameAuth.getLicenceNo(), password)
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
