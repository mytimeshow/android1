package cn.czyugang.tcg.client.api;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Address;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuzihong on 2017/10/30.
 */

public class AddressApi {
    /**
     * 加载地址列表
     *
     * @return
     */
    public static Observable<Response<List<Address>>> loadAddressList() {
        return UserOAuth.getInstance()
                .get("api/auth/v1/user/base/getUserAddress", null)
                .map(s -> (Response<List<Address>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, Address.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 添加地址
     *
     * @param name
     * @param mobile
     * @param sex
     * @param address
     * @param province
     * @param city
     * @param area
     * @param street
     * @param addressDetail
     * @param tag
     * @param lat
     * @param lon
     * @return
     */
    public Observable<Response> addAddress(String name, String mobile, String sex, String address,
                                           String province, String city, String area, String street,
                                           String addressDetail, String tag, double lat, double lon) {
        Map<String, Object> params = new HashMap<>();
        //联系人
        params.put("linkman", name);
        //手机号
        params.put("phone", mobile);
        //性别
        params.put("sex", sex);
        //地址
        params.put("building", address);
        //省
        params.put("province", province);
        //市
        params.put("city", city);
        //区
        params.put("area", area);
        //街道
        params.put("street", street);
        //详细地址
        params.put("address", addressDetail);
        //标签
        params.put("tag", tag);
        //坐标
        params.put("coordinates", lat + "," + lon);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/addUserAddress", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改地址
     *
     * @param name
     * @param mobile
     * @param sex
     * @param address
     * @param province
     * @param city
     * @param area
     * @param street
     * @param addressDetail
     * @param tag
     * @param lat
     * @param lon
     * @param defaultAddress
     * @return
     */
    public Observable<Response> editAddress(String id, String name, String mobile, String sex,
                                            String address, String province, String city,
                                            String area, String street, String addressDetail,
                                            String tag, double lat, double lon, String defaultAddress) {
        Map<String, Object> params = new HashMap<>();
        params.put("addressId", id);
        if (!TextUtils.isEmpty(name)) {
            //联系人
            params.put("linkman", name);
            //手机号
            params.put("phone", mobile);
            //性别
            params.put("sex", sex);
            //地址
            params.put("building", address);
            //省
            params.put("province", province);
            //市
            params.put("city", city);
            //区
            params.put("area", area);
            //街道
            params.put("street", street);
            //详细地址
            params.put("address", addressDetail);
            //标签
            params.put("tag", tag);
            //坐标
            params.put("coordinates", lat + "," + lon);
        }
        if (!TextUtils.isEmpty(defaultAddress)) {
            params.put("defaultAddress", defaultAddress);
        }
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/updateUserAddress", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    public Observable<Response> deleteAddress(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/delUserAddress", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
