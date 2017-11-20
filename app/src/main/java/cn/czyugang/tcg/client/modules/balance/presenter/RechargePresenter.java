package cn.czyugang.tcg.client.modules.balance.presenter;

import java.util.ArrayList;
import java.util.List;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.BankCardStyle;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.balance.contract.RechargeContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/11/2.
 */

public class RechargePresenter implements RechargeContract.Presenter {
    private RechargeContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BalanceApi mBalanceApi = new BalanceApi();

    private List<BankCardInfo> mBankCardList;
    private String mIsSetPayPassword;

    public RechargePresenter(RechargeContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        loadBankList();
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
                                mBankCardList = new ArrayList<>();
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
                                                mBankCardList.add(bankCardInfo);
                                            });
                                }
                                if (mBankCardList.size() > 0) {
                                    mView.updateBankInfo(mBankCardList.get(0));
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
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onRestart() {
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
                                mBankCardList = new ArrayList<>();
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
                                                mBankCardList.add(bankCardInfo);
                                            });
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
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
