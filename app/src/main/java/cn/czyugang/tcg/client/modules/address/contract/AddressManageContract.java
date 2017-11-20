package cn.czyugang.tcg.client.modules.address.contract;

import java.util.List;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;
import cn.czyugang.tcg.client.entity.Address;

/**
 * Created by wuzihong on 2017/10/25.
 */

public interface AddressManageContract {
    interface View extends BaseView {
        void updateAddressList(List<Address> addressList);
    }

    interface Presenter extends BasePresenter {
        void onResume();

        void deleteAddress(String addressId);

        void setDefaultAddress(String addressId);
    }
}
