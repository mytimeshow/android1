package cn.czyugang.tcg.client.modules.balance.presenter;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.BankCardStyle;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.contract.WithdrawContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/11/2.
 */

public class WithdrawPresenter implements WithdrawContract.Presenter {
    private WithdrawContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BalanceApi mBalanceApi = new BalanceApi();

    private List<BankCardInfo> mBankCardList;
    private String mIsSetPayPassword;

    public WithdrawPresenter(WithdrawContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    public void loadBankList() {
        mBalanceApi.loadBankList()
                .subscribe(new Observer<Response<List<BankCard>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<List<BankCard>> response) {
                        switch (response.getCode()) {
                            case 200:
                                List<BankCardInfo> bankCardInfoList = new ArrayList<>();
                                List<BankCard> bankCardList = response.getData();
                                List<BankCardStyle> bankCardStyleList = DictUtil.getDict(response.getValues(), "styleList", BankCardStyle.class);
                                mIsSetPayPassword = DictUtil.getObject(response.getValues(), "isSetPayPassword");
                                if (bankCardList != null) {
                                    Observable.fromIterable(bankCardList)
                                            .filter(bankCard -> bankCard != null)
                                            .subscribe(bankCard -> {
                                                BankCardInfo bankCardInfo = new BankCardInfo();
                                                bankCardInfo.setBankCard(bankCard);
                                                bankCardInfo.setBankCardStyle(DictUtil.lookupDict(bankCardStyleList, "bankName", bankCard.getOwnedBank()));
                                                bankCardInfoList.add(bankCardInfo);
                                            });
                                }
                                if (mBankCardList == null && bankCardInfoList.size() > 0) {
                                    mView.updateBankInfo(bankCardInfoList.get(0));
                                }
                                mBankCardList = bankCardInfoList;
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

    @Override
    public void onResume() {
        loadBankList();
    }

    @Override
    public List<BankCardInfo> getBankCardList() {
        return mBankCardList;
    }

    @Override
    public String getIsSetPayPassword() {
        return mIsSetPayPassword;
    }
}
