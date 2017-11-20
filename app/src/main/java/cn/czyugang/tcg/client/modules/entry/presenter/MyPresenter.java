package cn.czyugang.tcg.client.modules.entry.presenter;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.event.LogoutEvent;
import cn.czyugang.tcg.client.event.UpdateUserInfoEvent;
import cn.czyugang.tcg.client.modules.entry.contract.MyContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import cn.czyugang.tcg.client.utils.RxBus;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/9/13.
 * 个人中心的Presenter
 */

public class MyPresenter implements MyContract.Presenter {
    private MyContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private UserOAuth mUserOAuth = UserOAuth.getInstance();

    public MyPresenter(MyContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        if (mUserOAuth.isLogin()) {
            mView.updateUserInfo(mUserOAuth.getUserInfo());
        } else {
            mView.logout();
        }
        mCompositeDisposable.add(RxBus.getObservable(UpdateUserInfoEvent.class)
                .subscribe(updateUserInfoEvent -> {
                    mView.updateUserInfo(updateUserInfoEvent.getUserInfo());
                }));
        mCompositeDisposable.add(RxBus.getObservable(LogoutEvent.class)
                .subscribe(logoutEvent -> mView.logout()));
    }

    @Override
    public void dispose() {
    }

    @Override
    public void onResume() {
        if (mUserOAuth.isLogin()) {
            loadUserInfo();
        }
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        mUserApi.loadUserInfo()
                .subscribe(new Observer<Response<UserBase>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<UserBase> response) {
                        switch (response.getCode()) {
                            case 200:
                                UserInfo userInfo = mUserOAuth.getUserInfo();
                                if (userInfo == null) {
                                    userInfo = new UserInfo();
                                }
                                userInfo.setUserBase(response.getData());
                                userInfo.setUserDetail(DictUtil.getObject(response.getValues(), "userDetail", UserDetail.class));
                                userInfo.setTotalBalance(DictUtil.getObject(response.getValues(), "totalBalance"));
                                userInfo.setTotalScore(DictUtil.getObject(response.getValues(), "totalScore"));
                                userInfo.setTotalFetchCode(DictUtil.getObject(response.getValues(), "totalFetchCode"));
                                userInfo.setIsBindQQ(DictUtil.getObject(response.getValues(), "isBindQQ"));
                                userInfo.setIsBindWECHAT(DictUtil.getObject(response.getValues(), "isBindWECHAT"));
                                userInfo.setIsBindWEIBO(DictUtil.getObject(response.getValues(), "isBindWEIBO"));
                                userInfo.setSexDict(DictUtil.getStaticDict(response.getValues(), "sexDict"));
                                mUserOAuth.writeUserInfo(userInfo);
                                break;
                            default:
                                mView.showToast(response.getMessage());
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
