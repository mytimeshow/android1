package cn.czyugang.tcg.client.modules.set.presenter;

import android.text.TextUtils;

import cn.czyugang.tcg.client.api.UserApi;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.set.contract.ModifyPayPasswordContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/21.
 */

public class ModifyPayPasswordPresenter implements ModifyPayPasswordContract.Presenter {
    private ModifyPayPasswordContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private UserApi mUserApi = new UserApi();

    public ModifyPayPasswordPresenter(ModifyPayPasswordContract.View view) {
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
    public void modifyPayPassword(String oldPassword, String password, String againPassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            mView.showToast("请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mView.showToast("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(againPassword)) {
            mView.showToast("请再次输入新密码");
            return;
        }
        if (!password.equals(againPassword)) {
            mView.showToast("两次输入新密码不一致");
            return;
        }
        mUserApi.modifyPayPassword(oldPassword, password)
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
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingDialog();
                    }
                });
    }
}
