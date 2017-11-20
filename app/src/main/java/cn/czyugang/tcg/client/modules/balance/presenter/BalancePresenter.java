package cn.czyugang.tcg.client.modules.balance.presenter;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.entity.Balance;
import cn.czyugang.tcg.client.entity.BalanceInfo;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.contract.BalanceContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/25.
 */

public class BalancePresenter implements BalanceContract.Presenter {
    private BalanceContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BalanceApi mBalanceApi = new BalanceApi();
    private BalanceInfo mBalanceInfo;

    public BalancePresenter(BalanceContract.View view) {
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
    public void onResume() {
        loadBalanceInfo();
    }

    @Override
    public BalanceInfo getBalanceInfo() {
        return mBalanceInfo;
    }

    private void loadBalanceInfo() {
        mBalanceApi.loadBalanceInfo()
                .subscribe(new Observer<Response<Balance>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<Balance> response) {
                        switch (response.getCode()) {
                            case 200:
                                mBalanceInfo = new BalanceInfo();
                                mBalanceInfo.setBalance(response.getData());
                                mBalanceInfo.setBankCardCount(DictUtil.getObject(response.getValues(), "bankCardCount"));
                                mView.updateBalanceInfo(mBalanceInfo);
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
