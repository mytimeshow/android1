package cn.czyugang.tcg.client.api;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.czyugang.tcg.client.common.AppOAuth;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserToken;
import cn.czyugang.tcg.client.utils.string.EncryptUtils;
import cn.czyugang.tcg.client.utils.JsonParse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuzihong on 2017/9/18.
 * 用户相关接口
 */

public class UserApi {
    //业务类型
    public static final String TYPE_LOGIN = "LOGIN";//登录
    public static final String TYPE_PAY = "PAY";//支付
    public static final String TYPE_SHORTCUT_LOGIN = "QUICK_LOGIN";//快捷登录
    public static final String TYPE_FORGET_PASSWORD = "LOGIN_AND_FINDPASSWORD";//找回密码
    public static final String TYPE_BIND_MOBILE = "BIND_PHONE";//绑定手机
    public static final String TYPE_RESET_PASSWORD = "RESET_PASSWORD";//重置密码
    public static final String TYPE_BIND_MOBILE1 = "UPDATE_PHONE_STEP1";//当前手机号验证
    public static final String TYPE_BIND_MOBILE2 = "UPDATE_PHONE_STEP2";//修改新手机号验证
    public static final String TYPE_RESET_PAY_PASSWORD = "RESET_PAYPASSWORD";//重设支付密码
    public static final String TYPE_FORGET_PAY_PASSWORD = "FIND_PAYPASSWORD";//找回支付密码
    public static final String TYPE_BIND_EMAIL = "BIND_EMAIL";//绑定邮箱
    public static final String TYPE_UNBIND_EMAIL = "UNBIND_EMAIL";//解绑邮箱
    public static final String TYPE_BIND_BANK_CARD = "BIND_BANKCARD";//绑定银行卡
    //第三方类型
    public static final String TYPE_THIRDPARTY_WX = "WECHAT";
    public static final String TYPE_THIRDPARTY_TENCENT = "QQ";
    public static final String TYPE_THIRDPARTY_WEIBO = "WEIBO";
    //性别
    public static final String MAN = "MALE";
    public static final String WOMAN = "FEMALE";

    /**
     * 判断帐号是否需要验证图形验证码
     *
     * @param account
     * @param type
     * @return
     */
    public Observable<Response> checkNeedCode(String account, String type) {
        Map<String, Object> params = new HashMap<>();
        //帐号
        params.put("object", account);
        //业务类型
        params.put("type", type);
        return AppOAuth.getInstance()
                .post("api/auth/v1/oauth/checkNeedCode", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 帐号密码登录
     *
     * @param account
     * @param password
     * @param code
     * @return
     */
    public Observable<Response<UserToken>> accountLogin(String account, String password, String code) {
        String loginTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //帐号
        params.put("object", account);
        //密码 进行加密
        params.put("password", EncryptUtils.password(password, loginTime));
        if (!TextUtils.isEmpty(code)) {
            params.put("code", code);
        }
        //当前位置坐标
        params.put("location", "0,0");//TODO 修改定位坐标
        //时间戳
        params.put("loginTime", loginTime);
        //客户端 用户端
        params.put("loginType", "NORMAL");
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/login", params)
                .map(s -> (Response<UserToken>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, UserToken.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @param type   业务类型
     * @return
     */
    public Observable<Response> sendVerificationCode(String mobile, String type) {
        String loginTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("phoneNumber", mobile);
        //时间戳
        params.put("accessTime", loginTime);
        //业务类型
        params.put("type", type);
        //校验码
        params.put("accountAccessKey", EncryptUtils.checkCode(mobile + loginTime + type));
        return AppOAuth.getInstance()
                .post("api/auth/v1/bus/getCheckCode", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 快捷登录
     *
     * @param mobile
     * @param code
     * @return
     */
    public Observable<Response<UserToken>> shortcutLogin(String mobile, String code) {
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("phone", mobile);
        //验证码
        params.put("checkCode", code);
        //当前位置坐标
        params.put("location", "0,0");//TODO 修改定位坐标
        //客户端 用户端
        params.put("loginType", "NORMAL");
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/quickLogin", params)
                .map(s -> (Response<UserToken>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, UserToken.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置密码验证码检查
     *
     * @param mobile
     * @param code
     * @param type
     * @return
     */
    public Observable<Response> setPasswordCheckCode(String mobile, String code, String type) {
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("phone", mobile);
        //验证码
        params.put("checkCode", code);
        //业务类型
        params.put("codeType", type);
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/updateUserPasswordByPhoneFirst", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置密码
     *
     * @param mobile
     * @param password
     * @param type
     * @return
     */
    public Observable<Response> setPassword(String mobile, String password, String type) {
        String loginTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("phone", mobile);
        //密码 加密
        params.put("password", EncryptUtils.password(password, loginTime));
        //时间戳
        params.put("loginTime", loginTime);
        //业务类型
        params.put("codeType", type);
        if (TYPE_FORGET_PASSWORD.equals(type)) {
            return AppOAuth.getInstance()
                    .post("api/auth/v1/user/base/updateUserPasswordByPhoneNext", params)
                    .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return UserOAuth.getInstance()
                    .post("api/auth/v1/user/base/updateUserPasswordByPhoneNext", params)
                    .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    /**
     * 第三方登录
     *
     * @param authInfo 授权信息（openId）
     * @param type
     * @return
     */
    public Observable<Response<UserToken>> thirdpartyLogin(String authInfo, String authToken, String type) {
        Map<String, Object> params = new HashMap<>();
        //授权信息
        params.put("authInfo", authInfo);
        //授权Token
        params.put("accessToken", authToken);
        //授权类型
        params.put("authType", type);
        //当前位置坐标
        params.put("location", "0,0");//TODO 修改定位坐标
        //客户端 用户端
        params.put("loginType", "NORMAL");
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/authLogin", params)
                .map(s -> (Response<UserToken>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, UserToken.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 检查手机是否绑定第三方账号
     *
     * @param mobile
     * @param type
     * @return
     */
    public Observable<Response> checkMobileBindThirdparty(String mobile, String type) {
        Map<String, Object> params = new HashMap<>();
        //手机号
        params.put("phone", mobile);
        //授权类型
        params.put("authInfoType", type);
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/checkBindThirdPartyByPhone", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 第三方帐号绑定手机号码
     *
     * @param authInfo
     * @param mobile
     * @param code
     * @param type
     * @return
     */
    public Observable<Response<UserToken>> thirdpartyBindMobile(String authInfo, String accessToken, String mobile, String code, String type) {
        Map<String, Object> params = new HashMap<>();
        //授权信息
        params.put("authInfo", authInfo);
        //授权Token
        params.put("accessToken", accessToken);
        //手机号码
        params.put("phone", mobile);
        //验证码
        params.put("smsCheckCode", code);
        //授权类型
        params.put("authInfoType", type);
        //当前位置坐标
        params.put("location", "0,0");//TODO 修改定位坐标
        //客户端 用户端
        params.put("loginType", "NORMAL");
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/bindThirdPartyByPhone", params)
                .map(s -> (Response<UserToken>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, UserToken.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 加载用户信息
     *
     * @return
     */
    public Observable<Response<UserBase>> loadUserInfo() {
        return UserOAuth.getInstance()
                .get("api/auth/v1/user/base/getLoginInfo", null)
                .map(s -> (Response<UserBase>) JsonParse.fromJson(s, new JsonParse.Type(Response.class, UserBase.class)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 退出登录
     *
     * @return
     */
    public Observable<Response> logout() {
        Map<String, Object> params = new HashMap<>();
        //当前位置坐标
        params.put("location", "0,0");//TODO 修改定位坐标
        //客户端 用户端
        params.put("loginType", "NORMAL");
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/logout", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改用户信息
     *
     * @param fileId
     * @param nickname
     * @param sex
     * @param birthday
     * @return
     */
    public Observable<Response> modifyUserInfo(String fileId, String nickname, String sex, String birthday) {
        Map<String, Object> params = new HashMap<>();
        //头像文件id
        if (!TextUtils.isEmpty(fileId)) {
            params.put("fileId", fileId);
        }
        //昵称
        if (!TextUtils.isEmpty(nickname)) {
            params.put("nickname", nickname);
        }
        //性别
        if (!TextUtils.isEmpty(sex)) {
            params.put("sex", sex);
        }
        //生日
        if (!TextUtils.isEmpty(birthday)) {
            params.put("birthday", birthday);
        }
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/updateUserInfo", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改账号名
     *
     * @param account
     * @return
     */
    public Observable<Response> modifyAccount(String account) {
        Map<String, Object> params = new HashMap<>();
        //账号名
        params.put("account", account);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/updateAccount", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改手机号码验证码检查
     *
     * @param mobile
     * @param code
     * @return
     */
    public Observable<Response> modifyMobileCheckCode(String mobile, String code) {
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("oldPhone", mobile);
        //验证码
        params.put("smscheckCode", code);
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/updateBindUserPhoneFirst", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改手机号码
     *
     * @param mobile
     * @param code
     * @return
     */
    public Observable<Response> modifyMobile(String mobile, String code) {
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("newPhone", mobile);
        //验证码
        params.put("newSMScheckCode", code);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/updateBindUserPhoneNext", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 实名认证
     *
     * @param name
     * @param idCard
     * @return
     */
    public Observable<Response> realNameAuth(String name, String idCard) {
        String loginTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //姓名
        params.put("realName", name);
        //身份证
        String base64IdCard = EncryptUtils.encodeBase64(idCard);
        params.put("identityId", base64IdCard);
        //时间戳
        params.put("accessTime", loginTime);
        //校验码
        params.put("accessKey", EncryptUtils.checkCode(base64IdCard + loginTime));
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/realNameAuth", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Observable<Response> modifyPassword(String oldPassword, String newPassword) {
        String loginTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //旧密码
        params.put("oldPassword", EncryptUtils.password(oldPassword, loginTime));
        //新密码
        params.put("newPassword", EncryptUtils.password(newPassword, loginTime));
        //时间错
        params.put("loginTime", loginTime);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/updateUserPasswordByOldPassword", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置支付密码验证码验证／
     *
     * @param mobile
     * @param code
     * @param type
     * @return
     */
    public Observable<Response> setPayPasswordCheckCode(String mobile, String code, String type) {
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("phone", mobile);
        //验证码
        params.put("checkCode", code);
        //业务类型
        params.put("codeType", type);
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/updatePayPasswordByPhoneFirst", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置支付密码
     *
     * @param password
     * @param type
     * @return
     */
    public Observable<Response> setPayPassword(String password, String type) {
        String payTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //支付密码
        params.put("password", EncryptUtils.password(password, payTime));
        //时间戳
        params.put("payTime", payTime);
        //业务类型
        params.put("codeType", type);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/updatePayPasswordByPhone", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改支付密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Observable<Response> modifyPayPassword(String oldPassword, String newPassword) {
        String payTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //旧密码
        params.put("oldPassword", EncryptUtils.password(oldPassword, payTime));
        //新密码
        params.put("newPassword", EncryptUtils.password(newPassword, payTime));
        //时间错
        params.put("payTime", payTime);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/updateUserPayPasswordByOldPassword", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 绑定邮箱验证码验证
     *
     * @param mobile
     * @param code
     * @return
     */
    public Observable<Response> bindEmailCheckCode(String mobile, String code) {
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("phone", mobile);
        //验证码
        params.put("checkCode", code);
        return AppOAuth.getInstance()
                .post("api/auth/v1/user/base/bindUserEmailFirst", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    public Observable<Response> sendEmailVerificationCode(String email) {
        Map<String, Object> params = new HashMap<>();
        //邮箱
        params.put("email", email);
        return AppOAuth.getInstance()
                .post("api/auth/v1/bus/getEmailCheckCode", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 绑定邮箱
     *
     * @param email
     * @param code
     * @return
     */
    public Observable<Response> bindEmail(String email, String code) {
        Map<String, Object> params = new HashMap<>();
        //邮箱
        params.put("email", email);
        //邮箱验证码
        params.put("emailCheckCode", code);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/bindUserEmail", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 邮箱解绑
     *
     * @param mobile
     * @param code
     * @return
     */
    public Observable<Response> unbindEmail(String mobile, String code) {
        Map<String, Object> params = new HashMap<>();
        //手机号码
        params.put("phone", mobile);
        //验证码
        params.put("checkCode", code);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/unbindUserEmail", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 绑定第三方账号
     *
     * @param authInfo
     * @param type
     * @return
     */
    public Observable<Response> bindThirdparty(String authInfo, String type) {
        Map<String, Object> params = new HashMap<>();
        //授权信息
        params.put("authInfo", authInfo);
        //授权类型
        params.put("authInfoType", type);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/bindOtherAccount", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 解绑第三方账号
     *
     * @param type
     * @return
     */
    public Observable<Response> unbindThirdparty(String type) {
        Map<String, Object> params = new HashMap<>();
        //授权类型
        params.put("authInfoType", type);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/unBindOtherAccount", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 验证支付密码
     *
     * @param password
     * @return
     */
    public Observable<Response> verifyPayPassword(String password) {
        String payTime = String.valueOf(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        //支付密码
        params.put("payPassword", EncryptUtils.password(password, payTime));
        //时间戳
        params.put("payTime", payTime);
        return UserOAuth.getInstance()
                .post("api/auth/v1/user/base/validPayPassword", params)
                .map(s -> (Response) JsonParse.fromJson(s, Response.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
