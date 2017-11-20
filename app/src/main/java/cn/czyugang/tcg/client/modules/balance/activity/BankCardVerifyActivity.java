package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.RealNameAuth;
import cn.czyugang.tcg.client.modules.balance.contract.BankCardVerifyContract;
import cn.czyugang.tcg.client.modules.balance.presenter.BankCardVerifyPresenter;

/**
 * Created by wuzihong on 2017/10/17.
 * 银行卡验证
 */

public class BankCardVerifyActivity extends BaseActivity implements BankCardVerifyContract.View {
    public static final String KEY_BANK_CARD_INFO = "bankCardInfo";
    public static final String KEY_REAL_NAME_AUTH = "realNameAuth";
    public static final String KEY_PAY_PASSWORD = "payPassword";
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @BindView(R.id.tv_bank_number)
    TextView tv_bank_number;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_id_card)
    EditText et_id_card;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.cb_user_agreement)
    CheckBox cb_user_agreement;

    private BankCardVerifyContract.Presenter mPresenter;

    private BankCardInfo mBankCardInfo;
    private RealNameAuth mRealNameAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_verify);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mBankCardInfo = intent.getParcelableExtra(KEY_BANK_CARD_INFO);
        mRealNameAuth = intent.getParcelableExtra(KEY_REAL_NAME_AUTH);
        mPresenter = new BankCardVerifyPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        if (mBankCardInfo != null) {
            BankCard bankCard = mBankCardInfo.getBankCard();
            if (bankCard != null) {
                //银行+卡类型
                tv_bank.setText(bankCard.getOwnedBank() + bankCard.getCardType());
                //银行卡号
                tv_bank_number.setText(bankCard.getCardNum());
            }
        }
        if (mRealNameAuth != null) {
            //真实姓名
            et_name.setText(mRealNameAuth.getName());
            et_name.setKeyListener(null);
            //证件号码
            et_id_card.setText(mRealNameAuth.getLicenceNo());
            et_id_card.setKeyListener(null);
        }
    }

    @Override
    public void startBankMobileVerifyActivity(String mobile) {
        Intent intent = new Intent(context, BankMobileVerifyActivity.class);
        intent.putExtra(BankMobileVerifyActivity.KEY_MOBILE, mobile);
        intent.putExtra(BankMobileVerifyActivity.KEY_BANK_CARD_INFO, mBankCardInfo);
        intent.putExtra(BankMobileVerifyActivity.KEY_REAL_NAME_AUTH, mRealNameAuth);
        intent.putExtra(BankMobileVerifyActivity.KEY_PAY_PASSWORD, getIntent().getStringExtra(KEY_PAY_PASSWORD));
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.iv_back, R.id.iv_verify_explain, R.id.tv_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_verify_explain: {
                Intent intent = new Intent(context, ExplainActivity.class);
                intent.putExtra(ExplainActivity.KEY_TYPE, ExplainActivity.TYPE_BANK_CAR_VERIFY);
                startActivity(intent);
                break;
            }
            case R.id.tv_verify:
                mPresenter.verifyBankInfo(tv_bank_number.getText().toString(),
                        et_name.getText().toString(), et_id_card.getText().toString(),
                        et_mobile.getText().toString(), cb_user_agreement.isChecked());
                break;
        }
    }
}
