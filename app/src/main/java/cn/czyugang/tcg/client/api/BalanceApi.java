package cn.czyugang.tcg.client.api;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.czyugang.tcg.client.common.Config;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Balance;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.string.EncryptUtils;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuzihong on 2017/10/25.
 * 我的资产相关接口
 */

public class BalanceApi {
    public static final String TYPE_INCOME = "increase";
    public static final String TYPE_EXPENSE = "decrease";

    /**
     * 获取资产信息
     *
     * @return
     */
    public Observable<Response<Balance>> loadBalanceInfo() {
        return UserOAuth.getInstance()
                .get("/api/auth/v1/finance/info/balance", null)
                .map(s -> (Response<Balance>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Balance.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取银行卡列表
     *
     * @return
     */
    public Observable<Response<List<BankCard>>> loadBankList() {
        return UserOAuth.getInstance()
                .get("/api/auth/v1/finance/info/bankCardList", null)
                .map(s -> (Response<List<BankCard>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, BankCard.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 验证银行卡号
     *
     * @param bankNumber
     * @return
     */
    public Observable<Response<BankCard>> verifyBankNumber(String bankNumber) {
        String accessTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //银行卡号
        String base64BankNumber = EncryptUtils.encodeBase64(bankNumber);
        params.put("bankNo", base64BankNumber);
        //时间戳
        params.put("accessTime", accessTime);
        //校验码
        params.put("accountAccessKey", EncryptUtils.checkCode(base64BankNumber + accessTime));
        return UserOAuth.getInstance()
                .post("/api/auth/v1/user/finance/getBankCardInfo", params)
                .map(s -> (Response<BankCard>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, BankCard.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 验证银行卡信息
     *
     * @param bankNumber
     * @param name
     * @param idCard
     * @param mobile
     * @return
     */
    public Observable<Response> verifyBankInfo(String bankNumber, String name, String idCard, String mobile) {
        String accessTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //银行卡号
        String base64BankNumber = EncryptUtils.encodeBase64(bankNumber);
        params.put("bankcardId", base64BankNumber);
        //真实姓名
        params.put("cardOwner", name);
        //身份证
        String base64IdCard = EncryptUtils.encodeBase64(idCard);
        params.put("identityId", base64IdCard);
        //手机号
        params.put("phone", mobile);
        //时间戳
        params.put("accessTime", accessTime);
        //校验码
        params.put("accountAccessKey", EncryptUtils.checkCode(name + base64IdCard + base64BankNumber + mobile + accessTime));
        return UserOAuth.getInstance()
                .post("/api/auth/v1/user/finance/authBankCard", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 验证银行卡手机号码
     *
     * @param mobile
     * @param code
     * @param bank
     * @param bankNumber
     * @param bankType
     * @param name
     * @param idCard
     * @param payPassword
     * @return
     */
    public Observable<Response> verifyBankMobile(String mobile, String code, String bank, String bankNumber, String bankType, String name, String idCard, String payPassword) {
        Map<String, Object> params = new HashMap<>();
        //手机号
        params.put("phone", mobile);
        //验证码
        params.put("checkCode", code);
        //所属银行
        params.put("bank", bank);
        //银行卡号
        params.put("bankcardId", EncryptUtils.encodeBase64(bankNumber));
        //银行卡类型
        params.put("cardType", bankType);
        //真实姓名
        params.put("cardOwner", name);
        //身份证号
        params.put("identityId", EncryptUtils.encodeBase64(idCard));
        if (!TextUtils.isEmpty(payPassword)) {
            String payTime = String.valueOf(System.currentTimeMillis());
            //支付密码
            params.put("payPassword", EncryptUtils.password(payPassword, payTime));
            //时间戳
            params.put("payTime", payTime);
        }
        return UserOAuth.getInstance()
                .post("/api/auth/v1/user/finance/checkBankCardPhone", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response> bindBankCard(String mobile, String bank, String bankNumber, String bankType, String name, String idCard, String payPassword) {
        Map<String, Object> params = new HashMap<>();
        //手机号
        params.put("phone", mobile);
        //所属银行
        params.put("bank", bank);
        //银行卡号
        params.put("bankcardId", EncryptUtils.encodeBase64(bankNumber));
        //银行卡类型
        params.put("cardType", bankType);
        //真实姓名
        params.put("cardOwner", name);
        //身份证号
        params.put("identityId", EncryptUtils.encodeBase64(idCard));
        if (!TextUtils.isEmpty(payPassword)) {
            String payTime = String.valueOf(System.currentTimeMillis());
            //支付密码
            params.put("payPassword", EncryptUtils.password(payPassword, payTime));
            //时间戳
            params.put("payTime", payTime);
        }
        return UserOAuth.getInstance()
                .post("/api/auth/v1/user/finance/addUserBankCard", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 解绑银行卡
     *
     * @param bankId
     * @param password
     * @return
     */
    public Observable<Response> unbindBanKCard(String bankId, String password) {
        String payTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //银行卡id
        params.put("bankCardNum", bankId);
        //支付密码
        params.put("payPassword", EncryptUtils.password(password, payTime));
        //时间戳
        params.put("payTime", payTime);
        return UserOAuth.getInstance()
                .post("/api/auth/v1/user/finance/delUserBankCard", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 加载账单列表
     *
     * @param steamType
     * @param type
     * @param startTime
     * @param endTime
     * @param page
     * @param accessTime
     * @return
     */
    public Observable<Response<List<Bill>>> loadBillList(String steamType, String type, String month, String startTime, String endTime, int page, String accessTime) {
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(steamType)) {
            params.put("steamType", steamType);
        }
        if (!TextUtils.isEmpty(type)) {
            params.put("type", type);
        }
        if (!TextUtils.isEmpty(month)) {
            params.put("searchMonth", month);
        }
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        params.put("page", page);
        params.put("size", Config.PAGE_SIZE);
        if (!TextUtils.isEmpty(accessTime)) {
            params.put("accessTime", accessTime);
        }
        return UserOAuth.getInstance()
                .get("/api/auth/v1/finance/info/searchFundSteam", params)
                .map(s -> (Response<List<Bill>>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, new JsonParse.Type(List.class, Bill.class))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 加载账单详情
     *
     * @param billId
     * @return
     */
    public Observable<Response<Bill>> loadBillDetails(String billId) {
        Map<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        return UserOAuth.getInstance()
                .get("/api/auth/v1/finance/info/steamDetail", params)
                .map(s -> (Response<Bill>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, Bill.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
