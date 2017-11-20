package cn.czyugang.tcg.client.modules.login.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.entity.UserToken;
import cn.czyugang.tcg.client.modules.login.contract.AccountLoginContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/9/13.
 * 账户登录的Presenter
 */

public class AccountLoginPresenter implements AccountLoginContract.Presenter {
    private AccountLoginContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();

    public AccountLoginPresenter(AccountLoginContract.View view) {
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
    public void login(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            mView.showToast("请输入帐号或手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mView.showToast("请输入密码");
            return;
        }
        mUserApi.checkNeedCode(account, UserApi.TYPE_LOGIN)
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
                                mView.showCodeDialog(Config.getApiUrl() + DictUtil.getObject(response.getValues(), "codeUrl"));
                                mView.dismissLoadingDialog();
                                break;
                            default:
                                accountLogin(account, password, null);
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
                    }
                });
    }

    @Override
    public void login(String account, String password, String code) {
        if (TextUtils.isEmpty(account)) {
            mView.showToast("请输入帐号或手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mView.showToast("请输入密码");
            return;
        }
        if (code.length() != 4) {
            mView.showToast("请输入正确的验证码");
            return;
        }
        mView.dismissCodeDialog();
        accountLogin(account, password, code);
    }

    /**
     * 请求登录接口，处理返回数据
     *
     * @param account
     * @param password
     * @param code
     */
    private void accountLogin(String account, String password, String code) {
        mUserApi.accountLogin(account, password, code)
                .subscribe(new Observer<Response<UserToken>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response<UserToken> response) {
                        switch (response.getCode()) {
                            case 200:
                                UserInfo userInfo = new UserInfo();
                                userInfo.setUserBase(DictUtil.getObject(response.getValues(), "userBase", UserBase.class));
                                userInfo.setUserDetail(DictUtil.getObject(response.getValues(), "userDetail", UserDetail.class));
                                userInfo.setSexDict(DictUtil.getStaticDict(response.getValues(), "sexDict"));
                                UserOAuth.getInstance().login(response.getData(), userInfo);
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
