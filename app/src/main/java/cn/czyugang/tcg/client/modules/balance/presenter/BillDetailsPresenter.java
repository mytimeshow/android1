package cn.czyugang.tcg.client.modules.balance.presenter;

import java.util.List;

import cn.czyugang.tcg.client.api.BalanceApi;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.StaticDict;
import cn.czyugang.tcg.client.modules.balance.contract.BillDetailsContract;
import cn.czyugang.tcg.client.utils.DictUtil;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/28.
 */

public class BillDetailsPresenter implements BillDetailsContract.Presenter {
    private BillDetailsContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BalanceApi mBalanceApi = new BalanceApi();

    private String mBillId;

    public BillDetailsPresenter(BillDetailsContract.View view, String billId) {
        mView = view;
        mBillId = billId;
    }

    @Override
    public void subscribe() {
        loadBillDetails();
    }

    @Override
    public void dispose() {

    }

    private void loadBillDetails() {
        mBalanceApi.loadBillDetails(mBillId)
                .subscribe(new Observer<Response<Bill>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<Bill> response) {
                        switch (response.getCode()) {
                            case 200:
                                Bill bill = response.getData();
                                List<StaticDict> actionTypeDict = DictUtil.getStaticDict(response.getValues(), "actionTypeDict");
                                if (bill != null && actionTypeDict != null) {
                                    bill.setActionTypeStr(DictUtil.lookupStaticDict(actionTypeDict, bill.getActionType()));
                                }
                                mView.updateBill(bill);
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
