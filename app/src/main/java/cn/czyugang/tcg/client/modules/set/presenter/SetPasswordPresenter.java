package cn.czyugang.tcg.client.modules.set.presenter;

import android.text.TextUtils;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.contract.SetPasswordContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/21.
 */

public class SetPasswordPresenter implements SetPasswordContract.Presenter {
    private SetPasswordContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();

    private String mMobile;

    public SetPasswordPresenter(SetPasswordContract.View view, String mobile) {
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
    public void setPassword(String password, String againPassword) {
        if (TextUtils.isEmpty(password)) {
            mView.showToast("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(againPassword)) {
            mView.showToast("请再次输入密码");
            return;
        }
        if (!password.equals(againPassword)) {
            mView.showToast("两次输入密码不一致");
            return;
        }
        mUserApi.setPassword(mMobile, password, UserApi.TYPE_RESET_PASSWORD)
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
                                UserOAuth userOAuth = UserOAuth.getInstance();
                                UserInfo userInfo = userOAuth.getUserInfo();
                                if (userInfo != null) {
                                    UserDetail userDetail = userInfo.getUserDetail();
                                    if (userDetail != null) {
                                        userDetail.setSetPassword("YES");
                                        userOAuth.writeUserInfo(userInfo);
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
