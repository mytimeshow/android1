package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserDetail;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.set.contract.AccountSafeContract;
import cn.czyugang.tcg.client.modules.set.presenter.AccountSafePresenter;

/**
 * Created by wuzihong on 2017/10/4.
 * 账号与安全
 */

public class AccountSafeActivity extends BaseActivity implements AccountSafeContract.View {
    @BindView(R.id.fl_account)
    FrameLayout fl_account;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_auth)
    TextView tv_auth;

    private AccountSafeContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        ButterKnife.bind(this);
        mPresenter = new AccountSafePresenter(this);
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            UserBase userBase = userInfo.getUserBase();
            if (userBase != null) {
                //账号
                tv_account.setText(userBase.getAccount());
                if ("YES".equals(userBase.getUpdateAccount())) {
                    tv_account.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    fl_account.setClickable(false);
                } else {
                    tv_account.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_goto_arrow, 0);
                    fl_account.setClickable(true);
                }
                //手机号
                tv_mobile.setText(userBase.getPhone());
            }
            UserDetail userDetail = userInfo.getUserDetail();
            if (userDetail != null) {
                //实名状态
                if ("YES".equals(userDetail.getRealnameAuth())) {
                    tv_auth.setText("已认证");
                } else {
                    tv_auth.setText("未认证");
                }
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.fl_account, R.id.fl_mobile, R.id.fl_auth, R.id.tv_modify_password,
            R.id.tv_pay_password_manage, R.id.tv_bind_email, R.id.tv_bind_third_party})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_account: {
                Intent intent = new Intent(context, ModifyAccountActivity.class);
                UserInfo userInfo = mPresenter.getUserInfo();
                if (userInfo != null) {
                    UserBase userBase = userInfo.getUserBase();
                    if (userBase != null) {
                        intent.putExtra(ModifyAccountActivity.KEY_ACCOUNT, userBase.getAccount());
                    }
                }
                startActivity(intent);
                break;
            }
            case R.id.fl_mobile: {
                Intent intent = new Intent(context, CurrentBindMobileActivity.class);
                UserInfo userInfo = mPresenter.getUserInfo();
                if (userInfo != null) {
                    UserBase userBase = userInfo.getUserBase();
                    if (userBase != null) {
                        intent.putExtra(CurrentBindMobileActivity.KEY_MOBILE, userBase.getPhone());
                    }
                }
                startActivity(intent);
                break;
            }
            case R.id.fl_auth:
                startActivity(new Intent(context, RealNameAuthActivity.class));
                break;
            case R.id.tv_modify_password: {
                if ("YES".equals(mPresenter.getUserInfo().getUserDetail().getSetPassword())) {
                    //修改密码
                    Intent intent = new Intent(context, ModifyPasswordActivity.class);
                    UserInfo userInfo = mPresenter.getUserInfo();
                    if (userInfo != null) {
                        UserBase userBase = userInfo.getUserBase();
                        if (userBase != null) {
                            intent.putExtra(ModifyPasswordActivity.KEY_MOBILE, userBase.getPhone());
                        }
                    }
                    startActivity(intent);
                } else {
                    //设置密码
                    Intent intent = new Intent(context, MobileVerifyActivity.class);
                    intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_SET_PASSWORD);
                    UserInfo userInfo = mPresenter.getUserInfo();
                    if (userInfo != null) {
                        UserBase userBase = userInfo.getUserBase();
                        if (userBase != null) {
                            intent.putExtra(MobileVerifyActivity.KEY_MOBILE, userBase.getPhone());
                        }
                    }
                    startActivity(intent);
                }
                break;
            }
            case R.id.tv_pay_password_manage: {
                if ("YES".equals(mPresenter.getUserInfo().getUserDetail().getSetPayPassword())) {
                    //支付密码管理
                    Intent intent = new Intent(context, PayPasswordManageActivity.class);
                    UserInfo userInfo = mPresenter.getUserInfo();
                    if (userInfo != null) {
                        UserBase userBase = userInfo.getUserBase();
                        if (userBase != null) {
                            intent.putExtra(PayPasswordManageActivity.KEY_MOBILE, userBase.getPhone());
                        }
                    }
                    startActivity(intent);
                } else {
                    //设置支付密码
                    Intent intent = new Intent(context, MobileVerifyActivity.class);
                    intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_SET_PAY_PASSWORD);
                    UserInfo userInfo = mPresenter.getUserInfo();
                    if (userInfo != null) {
                        UserBase userBase = userInfo.getUserBase();
                        if (userBase != null) {
                            intent.putExtra(MobileVerifyActivity.KEY_MOBILE, userBase.getPhone());
                        }
                    }
                    startActivity(intent);
                }
                break;
            }
            case R.id.tv_bind_email: {
                if (TextUtils.isEmpty(mPresenter.getUserInfo().getUserBase().getEmail())) {
                    //绑定邮箱
                    Intent intent = new Intent(context, MobileVerifyActivity.class);
                    intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_BIND_EMAIL);
                    UserInfo userInfo = mPresenter.getUserInfo();
                    if (userInfo != null) {
                        UserBase userBase = userInfo.getUserBase();
                        if (userBase != null) {
                            intent.putExtra(MobileVerifyActivity.KEY_MOBILE, userBase.getPhone());
                        }
                    }
                    startActivity(intent);
                } else {
                    //解绑邮箱
                    Intent intent = new Intent(context, UnbindEmailActivity.class);
                    UserInfo userInfo = mPresenter.getUserInfo();
                    if (userInfo != null) {
                        UserBase userBase = userInfo.getUserBase();
                        if (userBase != null) {
                            intent.putExtra(UnbindEmailActivity.KEY_EMAIL, userBase.getEmail());
                            intent.putExtra(UnbindEmailActivity.KEY_MOBILE, userBase.getPhone());
                        }
                    }
                    startActivity(intent);
                }
                break;
            }
            case R.id.tv_bind_third_party:
                startActivity(new Intent(context, BindThirdpartyActivity.class));
                break;
        }
    }
}
