package cn.czyugang.tcg.client.modules.set.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.RealNameAuth;
import cn.czyugang.tcg.client.modules.set.contract.SetPayPasswordContract;
import cn.czyugang.tcg.client.modules.set.presenter.SetPayPasswordPresenter;
import cn.czyugang.tcg.client.widget.PayPasswordEditText;

/**
 * Created by wuzihong on 2017/10/6.
 * 设置支付密码
 */

public class SetPayPasswordActivity extends BaseActivity implements SetPayPasswordContract.View, PayPasswordEditText.OnEntryCompleteListener {
    public static final String KEY_TYPE = "type";
    public static final String KEY_BANK_MOBILE = "bankMobile";
    public static final String KEY_BANK_CARD_INFO = "bankCardInfo";
    public static final String KEY_REAL_NAME_AUTH = "realNameAuth";
    public static final int TYPE_SET_PAY_PASSWORD = 1;//设置支付密码
    public static final int TYPE_FORGET_PAY_PASSWORD = 2;//找回支付密码
    public static final int TYPE_BIND_BANK_CARD = 3;//绑定银行卡
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.payPasswordEditText)
    PayPasswordEditText payPasswordEditText;

    private SetPayPasswordContract.Presenter mPresenter;

    private int mType;
    private String mBankMobile;
    private BankCardInfo mBankCardInfo;
    private RealNameAuth mRealNameAuth;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_password);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mType = intent.getIntExtra(KEY_TYPE, 0);
        mBankMobile = intent.getStringExtra(KEY_BANK_MOBILE);
        mBankCardInfo = intent.getParcelableExtra(KEY_BANK_CARD_INFO);
        mRealNameAuth = intent.getParcelableExtra(KEY_REAL_NAME_AUTH);
        mPresenter = new SetPayPasswordPresenter(this, mType);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        switch (mType) {
            case TYPE_SET_PAY_PASSWORD:
            case TYPE_BIND_BANK_CARD:
                initSetPayPassword();
                break;
            case TYPE_FORGET_PAY_PASSWORD:
                initForgetPayPassword();
                break;
        }
        payPasswordEditText.setOnEntryCompleteListener(this);
    }

    private void initSetPayPassword() {
        tv_title.setText("设置支付密码");
        tv_hint.setText("设置支付密码，用于支付/提现验证");
    }

    private void initForgetPayPassword() {
        tv_title.setText("找回支付密码");
        tv_hint.setText("请输入6位数字支付密码");
    }

    @Override
    public void clearPassword() {
        mPassword = null;
        payPasswordEditText.setText(null);
        switch (mType) {
            case TYPE_SET_PAY_PASSWORD:
            case TYPE_BIND_BANK_CARD:
                initSetPayPassword();
                break;
            case TYPE_FORGET_PAY_PASSWORD:
                initForgetPayPassword();
                break;
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onEntryComplete(String password) {
        if (TextUtils.isEmpty(mPassword)) {
            mPassword = password;
            payPasswordEditText.setText(null);
            tv_hint.setText("请再次输入支付密码");
        } else {
            if (mType == TYPE_BIND_BANK_CARD) {
                mPresenter.bindBankCard(mPassword, password, mBankCardInfo, mRealNameAuth, mBankMobile);
                return;
            }
            mPresenter.setPayPassword(mPassword, password);
        }
    }
}
