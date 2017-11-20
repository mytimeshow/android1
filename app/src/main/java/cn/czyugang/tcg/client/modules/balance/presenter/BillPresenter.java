package cn.czyugang.tcg.client.modules.balance.presenter;

import java.util.List;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.entity.BillStatus;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.StaticDict;
import cn.czyugang.tcg.client.modules.balance.contract.BillContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/27.
 */

public class BillPresenter implements BillContract.Presenter {
    private BillContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BalanceApi mBalanceApi = new BalanceApi();

    private List<Bill> mBillList;
    private int mCurrentPage;
    private String mAccessTime;

    public BillPresenter(BillContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        loadBillList(0, null);
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void loadMoreBillList() {
        loadBillList(mCurrentPage + 1, mAccessTime);
    }

    /**
     * 加载账单列表
     *
     * @param page
     * @param accessTime
     */
    private void loadBillList(int page, String accessTime) {
        mBalanceApi.loadBillList(null, null, null, null, null, page, accessTime)
                .subscribe(new Observer<Response<List<Bill>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<List<Bill>> response) {
                        switch (response.getCode()) {
                            case 200:
                                mCurrentPage = response.getCurrentPage();
                                mAccessTime = response.getAccessTime();
                                if (mBillList == null || mCurrentPage == 0) {
                                    mBillList = response.getData();
                                } else {
                                    mBillList.addAll(response.getData());
                                }
                                List<StaticDict> actionTypeDict = DictUtil.getStaticDict(response.getValues(), "actionTypeDict");
                                List<BillStatus> billStatusDict = DictUtil.getStaticDict(response.getValues(), "statusList");
                                if (mBillList != null) {
                                    Observable.fromIterable(mBillList)
                                            .filter(bill -> bill != null)
                                            .subscribe(bill -> {
                                                if (actionTypeDict != null) {
                                                    bill.setActionTypeStr(DictUtil.lookupStaticDict(actionTypeDict, bill.getActionType()));
                                                }
                                                if (billStatusDict != null) {
                                                    BillStatus billStatus = DictUtil.lookupDict(billStatusDict, "fundSteamId", bill.getId());
                                                    if (billStatus != null) {
                                                        bill.setActionTypeStr(billStatus.getStatusText());
                                                    }
                                                }
                                            });
                                }
                                mView.updateBillList(mBillList, response.getTotalPage() > mCurrentPage);
                                mView.updateIncomeExpense(DictUtil.getObject(response.getValues(), "totalIncome"),
                                        DictUtil.getObject(response.getValues(), "totalPay"));
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
