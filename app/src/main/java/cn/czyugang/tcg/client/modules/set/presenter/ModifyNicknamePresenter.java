package cn.czyugang.tcg.client.modules.set.presenter;

import android.text.TextUtils;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.contract.ModifyNicknameContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/20.
 */

public class ModifyNicknamePresenter implements ModifyNicknameContract.Presenter {
    private ModifyNicknameContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();

    public ModifyNicknamePresenter(ModifyNicknameContract.View view) {
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
    public void modifyNickname(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            mView.showToast("请输入昵称");
            return;
        }
        mUserApi.modifyUserInfo(null, nickname, null, null)
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
                                    UserBase userBase = userInfo.getUserBase();
                                    if (userBase != null) {
                                        userInfo.getUserBase().setNickname(nickname);
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
