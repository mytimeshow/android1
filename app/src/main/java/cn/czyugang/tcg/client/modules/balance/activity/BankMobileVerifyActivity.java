package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
import cn.czyugang.tcg.client.modules.balance.contract.BankMobileVerifyContract;
import cn.czyugang.tcg.client.modules.balance.presenter.BankMobileVerifyPresenter;
import cn.czyugang.tcg.client.modules.set.activity.MobileVerifyActivity;
import cn.czyugang.tcg.client.modules.set.activity.SetPayPasswordActivity;

/**
 * Created by wuzihong on 2017/10/17.
 * 银行卡手机验证码
 */

public class BankMobileVerifyActivity extends BaseActivity implements BankMobileVerifyContract.View {
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_BANK_CARD_INFO = "bankCardInfo";
    public static final String KEY_REAL_NAME_AUTH = "realNameAuth";
    public static final String KEY_PAY_PASSWORD = "payPassword";
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.et_verification_code)
    EditText et_verification_code;
    @BindView(R.id.tv_send_verification_code)
    TextView tv_send_verification_code;
    private Resources mResources;

    private BankMobileVerifyContract.Presenter mPresenter;

    private String mMobile;
    private BankCardInfo mBankCardInfo;
    private RealNameAuth mRealNameAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_mobile_verify);
        ButterKnife.bind(this);
        mResources = getResources();
        Intent intent = getIntent();
        mMobile = intent.getStringExtra(KEY_MOBILE);
        mBankCardInfo = intent.getParcelableExtra(KEY_BANK_CARD_INFO);
        mRealNameAuth = intent.getParcelableExtra(KEY_REAL_NAME_AUTH);
        mPresenter = new BankMobileVerifyPresenter(this, mMobile, intent.getStringExtra(KEY_PAY_PASSWORD));
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        tv_mobile.setText(String.format("请输入手机 %s 收到的短信验证码", mMobile));
    }

    @Override
    public void updateVerificationCodeCountdown(int time) {
        if (time == 0) {
            tv_send_verification_code.setText("获取验证码");
            tv_send_verification_code.setEnabled(true);
            //设置为可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.text_main_button_red_border_normal));
            tv_send_verification_code.setBackgroundResource(R.drawable.bg_main_button_red_border_normal);
        } else {
            tv_send_verification_code.setText(String.format("%ds后重新获取", time));
            tv_send_verification_code.setEnabled(false);
            //设置为不可用样式
            tv_send_verification_code.setTextColor(mResources.getColor(R.color.text_main_button_red_border_disable));
            tv_send_verification_code.setBackgroundResource(R.drawable.bg_main_button_red_border_disable);
        }
    }

    @Override
    public void startMobileVerifyActivity(String mobile) {
        Intent intent = new Intent(context, MobileVerifyActivity.class);
        intent.putExtra(MobileVerifyActivity.KEY_TYPE, MobileVerifyActivity.TYPE_BIND_BANK_CARD);
        intent.putExtra(MobileVerifyActivity.KEY_MOBILE, mobile);
        intent.putExtra(SetPayPasswordActivity.KEY_BANK_MOBILE, mMobile);
        intent.putExtra(SetPayPasswordActivity.KEY_BANK_CARD_INFO, mBankCardInfo);
        intent.putExtra(SetPayPasswordActivity.KEY_REAL_NAME_AUTH, mRealNameAuth);
        startActivity(intent);
        finish();
    }

    @Override
    public void startSetPayPasswordActivity() {
        Intent intent = new Intent(context, SetPayPasswordActivity.class);
        intent.putExtra(SetPayPasswordActivity.KEY_TYPE, SetPayPasswordActivity.TYPE_BIND_BANK_CARD);
        intent.putExtra(SetPayPasswordActivity.KEY_BANK_MOBILE, mMobile);
        intent.putExtra(SetPayPasswordActivity.KEY_BANK_CARD_INFO, mBankCardInfo);
        intent.putExtra(SetPayPasswordActivity.KEY_REAL_NAME_AUTH, mRealNameAuth);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.iv_back, R.id.tv_send_verification_code, R.id.tv_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_verification_code:
                mPresenter.sendVerificationCode();
                break;
            case R.id.tv_verify: {
                BankCard bankCard = mBankCardInfo.getBankCard();
                mPresenter.commitVerificationCode(et_verification_code.getText().toString(), bankCard.getOwnedBank(), bankCard.getCardNum(), bankCard.getCardType(), mRealNameAuth.getName(), mRealNameAuth.getLicenceNo());
                break;
            }
        }
    }
}
