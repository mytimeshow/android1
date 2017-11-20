package cn.czyugang.tcg.client.modules.address.presenter;

import java.util.List;

import cn.czyugang.tcg.client.api.AddressApi;
import cn.czyugang.tcg.client.entity.Address;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.address.contract.AddressManageContract;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/10/30.
 */

public class AddressManagePresenter implements AddressManageContract.Presenter {
    private AddressManageContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private AddressApi mAddressApi = new AddressApi();

    public AddressManagePresenter(AddressManageContract.View view) {
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
        loadAddressList();
    }

    @Override
    public void deleteAddress(String addressId) {
        mAddressApi.deleteAddress(addressId)
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
                                loadAddressList();
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
    public void setDefaultAddress(String addressId) {
        mAddressApi.editAddress(addressId, null, null, null, null, null, null, null, null, null, null, 0, 0, "YES")
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
                                loadAddressList();
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

    private void loadAddressList() {
        mAddressApi.loadAddressList()
                .subscribe(new Observer<Response<List<Address>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<List<Address>> response) {
                        switch (response.getCode()) {
                            case 200:
                                mView.updateAddressList(response.getData());
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
