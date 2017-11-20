package cn.czyugang.tcg.client.modules.set.presenter;

import android.text.TextUtils;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.contract.RealNameAuthContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/20.
 */

public class RealNameAuthPresenter implements RealNameAuthContract.Presenter {
    private RealNameAuthContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();
    private UserOAuth mUserOAuth = UserOAuth.getInstance();

    public RealNameAuthPresenter(RealNameAuthContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        mView.updateUserInfo(mUserOAuth.getUserInfo());
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void auth(String name, String idCard) {
        if (TextUtils.isEmpty(name)) {
            mView.showToast("请输入真实姓名");
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            mView.showToast("请输入居民身份证号");
            return;
        }
        mUserApi.realNameAuth(name, idCard)
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
                                UserInfo userInfo = mUserOAuth.getUserInfo();
                                if (userInfo != null) {
                                    UserBase userBase = userInfo.getUserBase();
                                    if (userBase != null) {
                                        userBase.setName(name);
                                    }
                                    UserDetail userDetail = userInfo.getUserDetail();
                                    if (userDetail != null) {
                                        userDetail.setRealnameAuth("YES");
                                        userDetail.setIdentityCardId(idCard);
                                    }
                                    if (userBase != null || userDetail != null) {
                                        mUserOAuth.writeUserInfo(userInfo);
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
}
