package cn.czyugang.tcg.client.modules.set.presenter;

import java.io.File;
import java.util.Date;

import cn.czyugang.tcg.client.api.AppApi;
import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UploadFile;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.event.UpdateUserInfoEvent;
import cn.czyugang.tcg.client.modules.set.contract.AccountInfoContract;
import cn.czyugang.tcg.client.utils.DateFormat;
import cn.czyugang.tcg.client.utils.RxBus;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/19.
 */

public class AccountInfoPresenter implements AccountInfoContract.Presenter {
    private AccountInfoContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private AppApi mAppApi = new AppApi();
    private UserApi mUserApi = new UserApi();
    private UserOAuth mUserOAuth = UserOAuth.getInstance();

    public AccountInfoPresenter(AccountInfoContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        mView.updateUserInfo(mUserOAuth.getUserInfo());
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
    public void uploadAvatar(File file) {
        if (file == null || !file.exists()) {
            mView.showToast("文件不存在");
            return;
        }
        mAppApi.uploadFile(file, AppApi.TYPE_AVATAR)
                .subscribe(new Observer<Response<UploadFile>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                        mView.showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull Response<UploadFile> response) {
                        switch (response.getCode()) {
                            case 200:
                                modifyAvatar(response.getData().getId());
                                break;
                            default:
                                mView.showToast(response.getMessage());
                                mView.dismissLoadingDialog();
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
    public void modifySexToMan() {
        modifySex(UserApi.MAN);
    }

    @Override
    public void modifySexToWoman() {
        modifySex(UserApi.WOMAN);
    }

    @Override
    public void modifyBirthday(Date birthday) {
        mUserApi.modifyUserInfo(null, null, null, DateFormat.format(birthday))
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
                                    UserDetail userDetail = userInfo.getUserDetail();
                                    if (userDetail != null) {
                                        userDetail.setBirthday(birthday);
                                        mUserOAuth.writeUserInfo(userInfo);
                                    }
                                }
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

    @Override
    public UserInfo getUserInfo() {
        return mUserOAuth.getUserInfo();
    }

    private void modifyAvatar(String fileId) {
        mUserApi.modifyUserInfo(fileId, null, null, null)
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
                                    UserDetail userDetail = userInfo.getUserDetail();
                                    if (userDetail != null) {
                                        userDetail.setFileId(fileId);
                                        mUserOAuth.writeUserInfo(userInfo);
                                    }
                                }
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

    private void modifySex(String sex) {
        mUserApi.modifyUserInfo(null, null, sex, null)
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
                                        userBase.setSex(sex);
                                        mUserOAuth.writeUserInfo(userInfo);
                                    }
                                }
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
