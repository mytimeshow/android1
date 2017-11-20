package cn.czyugang.tcg.client.modules.set.presenter;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.event.UpdateUserInfoEvent;
import cn.czyugang.tcg.client.modules.set.contract.SetContract;
import cn.czyugang.tcg.client.utils.RxBus;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/9/13.
 */

public class SetPresenter implements SetContract.Presenter {
    private SetContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private UserOAuth mUserOAuth = UserOAuth.getInstance();

    public SetPresenter(SetContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        if (mUserOAuth.isLogin()) {
            mView.updateUserInfo(mUserOAuth.getUserInfo());
        } else {
            mView.hideUserInfo();
        }
        RxBus.getObservable(UpdateUserInfoEvent.class)
                .subscribe(updateUserInfoEvent -> {
                    mView.updateUserInfo(updateUserInfoEvent.getUserInfo());
                });
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void logout() {
        mUserApi.logout()
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
                                mUserOAuth.logout();
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
