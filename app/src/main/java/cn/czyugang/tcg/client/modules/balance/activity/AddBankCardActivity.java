package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.BankCardStyle;
import cn.czyugang.tcg.client.entity.RealNameAuth;
import cn.czyugang.tcg.client.modules.balance.contract.AddBankCardContract;
import cn.czyugang.tcg.client.modules.balance.presenter.AddBankCardPresenter;
import cn.czyugang.tcg.client.utils.ImageLoader;

/**
 * Created by wuzihong on 2017/10/17.
 * 添加银行卡
 */

public class AddBankCardActivity extends BaseActivity implements AddBankCardContract.View, TextWatcher {
    public static final String KEY_PAY_PASSWORD = "payPassword";
    @BindView(R.id.et_bank_number)
    EditText et_bank_number;
    @BindView(R.id.ll_bank)
    LinearLayout ll_bank;
    @BindView(R.id.fresco_bank_logo)
    SimpleDraweeView fresco_bank_logo;
    @BindView(R.id.tv_bank)
    TextView tv_bank;

    private AddBankCardContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        ButterKnife.bind(this);
        mPresenter = new AddBankCardPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        et_bank_number.addTextChangedListener(this);
    }

    @Override
    public void updateBankCardInfo(BankCardInfo bankCardInfo) {
        if (bankCardInfo != null) {
            BankCardStyle bankCardStyle = bankCardInfo.getBankCardStyle();
            if (bankCardStyle != null) {
                ImageLoader.loadImageToView(bankCardStyle.getIconId(), fresco_bank_logo);
            }
            BankCard bankCard = bankCardInfo.getBankCard();
            if (bankCard != null) {
                //银行+卡类型
                tv_bank.setText(bankCard.getOwnedBank() + bankCard.getCardType());
            }
        }
        ll_bank.setVisibility(View.VISIBLE);
    }

    @Override
    public void startBankCardVerifyActivity(BankCardInfo bankCardInfo, RealNameAuth realNameAuth) {
        Intent intent = new Intent(context, BankCardVerifyActivity.class);
        intent.putExtra(BankCardVerifyActivity.KEY_BANK_CARD_INFO, bankCardInfo);
        intent.putExtra(BankCardVerifyActivity.KEY_REAL_NAME_AUTH, realNameAuth);
        intent.putExtra(BankCardVerifyActivity.KEY_PAY_PASSWORD, getIntent().getStringExtra(KEY_PAY_PASSWORD));
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.iv_back, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                mPresenter.gotoNext(et_bank_number.getText().toString());
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 16 || s.length() == 19) {
            mPresenter.verifyBankNumber(s.toString());
        } else {
            ll_bank.setVisibility(View.GONE);
            mPresenter.disposeVerifyBankNumber();
        }
    }
}
