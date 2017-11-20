package cn.czyugang.tcg.client.modules.set.presenter;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.utils.rxbus.UpdateUserInfoEvent;
import cn.czyugang.tcg.client.modules.set.contract.AccountSafeContract;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by wuzihong on 2017/10/20.
 */

public class AccountSafePresenter implements AccountSafeContract.Presenter {
    private AccountSafeContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserOAuth mUserOAuth = UserOAuth.getInstance();

    public AccountSafePresenter(AccountSafeContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        mView.updateUserInfo(mUserOAuth.getUserInfo());
        mCompositeDisposable.add(RxBus.getObservable(UpdateUserInfoEvent.class)
                .subscribe(updateUserInfoEvent -> {
                    mView.updateUserInfo(updateUserInfoEvent.getUserInfo());
                }));
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserOAuth.getUserInfo();
    }
}
