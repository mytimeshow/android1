package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.UserOAuth;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.UserBase;
import cn.czyugang.tcg.client.entity.UserInfo;
import cn.czyugang.tcg.client.modules.balance.contract.AddBankCardPasswordContract;
import cn.czyugang.tcg.client.modules.balance.presenter.AddBankCardPasswordPresenter;
import cn.czyugang.tcg.client.modules.set.activity.MobileVerifyActivity;
import cn.czyugang.tcg.client.widget.PayPasswordEditText;

/**
 * Created by wuzihong on 2017/10/16.
 * 解除银行卡绑定支付密码
 */

public class AddBankCardPasswordActivity extends BaseActivity implements
        AddBankCardPasswordContract.View, PayPasswordEditText.OnEntryCompleteListener {
    public static final String KEY_TYPE = "type";
    public static final String KEY_BANK_CARD_INFO = "bankCardInfo";
    public static final int TYPE_ADD_BANK_CARD = 0;//添加银行卡
    public static final int TYPE_UNBIND_BANK_CARD = 1;//解绑银行卡
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.payPasswordEditText)
    PayPasswordEditText payPasswordEditText;

    private AddBankCardPasswordContract.Presenter mPresenter;

    private int mType;
    private BankCardInfo mBankCardInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card_password);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mType = intent.getIntExtra(KEY_TYPE, 0);
        mBankCardInfo = intent.getParcelableExtra(KEY_BANK_CARD_INFO);
        mPresenter = new AddBankCardPasswordPresenter(this, mBankCardInfo);
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
            case TYPE_ADD_BANK_CARD:
                tv_hint.setText("请输入支付密码，以验证身份");
                break;
            case TYPE_UNBIND_BANK_CARD:
                tv_hint.setText("请输入支付密码，解除银行卡绑定");
                break;
        }
        payPasswordEditText.setOnEntryCompleteListener(this);
    }

    @Override
    public void startAddBankCardActivity(String password) {
        Intent intent = new Intent(context, AddBankCardActivity.class);
        intent.putExtra(AddBankCardActivity.KEY_PAY_PASSWORD, password);
        startActivity(intent);
        finish();
    }

    @Override
    public void clearPassword() {
        payPasswordEditText.setText(null);
    }

    @OnClick({R.id.iv_back, R.id.tv_forgot_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_forgot_password: {
                Intent intent = new Intent(context, MobileVerifyActivity.class);
                intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_FORGET_PAY_PASSWORD);
                UserInfo userInfo = UserOAuth.getInstance().getUserInfo();
                if (userInfo != null) {
                    UserBase userBase = userInfo.getUserBase();
                    if (userBase != null) {
                        intent.putExtra(MobileVerifyActivity.KEY_MOBILE, userBase.getPhone());
                    }
                }
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onEntryComplete(String password) {
        switch (mType) {
            case TYPE_ADD_BANK_CARD:
                mPresenter.verifyPayPassword(password);
                break;
            case TYPE_UNBIND_BANK_CARD:
                mPresenter.unbindBanKCard(password);
                break;
        }
    }
}
