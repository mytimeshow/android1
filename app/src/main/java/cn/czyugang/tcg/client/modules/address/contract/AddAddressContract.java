package cn.czyugang.tcg.client.modules.address.contract;

import cn.czyugang.tcg.client.base.BasePresenter;
import cn.czyugang.tcg.client.base.BaseView;

/**
 * Created by wuzihong on 2017/11/1.
 */

public interface AddAddressContract {
    interface View extends BaseView {
        void finish();
    }

    interface Presenter extends BasePresenter {
        void saveAddress(String name, String mobile, String sex, String address, String province,
                         String city, String area, String street, String addressDetail, String tag,
                         double lat, double lon);

        void deleteAddress();
    }
}
