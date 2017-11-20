package cn.czyugang.tcg.client.modules.address.presenter;

import android.text.TextUtils;

import cn.czyugang.tcg.client.api.AddressApi;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.modules.address.contract.AddAddressContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuzihong on 2017/11/1.
 */

public class AddAddressPresenter implements AddAddressContract.Presenter {
    private AddAddressContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private AddressApi mAddressApi = new AddressApi();

    private String mAddressId;

    public AddAddressPresenter(AddAddressContract.View view, String addressId) {
        mView = view;
        mAddressId = addressId;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void saveAddress(String name, String mobile, String sex, String address, String province,
                            String city, String area, String street, String addressDetail,
                            String tag, double lat, double lon) {
        if (TextUtils.isEmpty(name)) {
            mView.showToast("请输入收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            mView.showToast("请输入手机号码");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            mView.showToast("请选择收货地址");
            return;
        }
        Observable<Response> observable;
        if (TextUtils.isEmpty(mAddressId)) {
            observable = mAddressApi.addAddress(name, mobile, sex, address, province, city, area, street, addressDetail, tag, lat, lon);
        } else {
            observable = mAddressApi.editAddress(mAddressId, name, mobile, sex, address, province, city, area, street, addressDetail, tag, lat, lon, null);
        }
        observable.subscribe(new Observer<Response>() {
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

    @Override
    public void deleteAddress() {
        mAddressApi.deleteAddress(mAddressId)
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
