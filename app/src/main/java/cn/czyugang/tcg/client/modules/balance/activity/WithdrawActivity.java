package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.BankCard;
import cn.czyugang.tcg.client.entity.BankCardInfo;
import cn.czyugang.tcg.client.entity.BankCardStyle;
import cn.czyugang.tcg.client.modules.balance.contract.WithdrawContract;
import cn.czyugang.tcg.client.modules.balance.presenter.WithdrawPresenter;
import cn.czyugang.tcg.client.utils.img.ImageLoader;

/**
 * Created by wuzihong on 2017/10/16.
 * 提现
 */

public class WithdrawActivity extends BaseActivity implements WithdrawContract.View {
    private final int REQUEST_SELECT_BANK_CARD = 0;
    public static final String KEY_BALANCE = "balance";
    @BindView(R.id.fresco_bank_logo)
    SimpleDraweeView fresco_bank_logo;
    @BindView(R.id.fl_add_bank_card)
    FrameLayout fl_add_bank_card;
    @BindView(R.id.rl_bank)
    ConstraintLayout rl_bank;
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @BindView(R.id.tv_bank_number)
    TextView tv_bank_number;
    @BindView(R.id.tv_withdraw_max_money)
    TextView tv_withdraw_max_money;
    @BindView(R.id.et_withdraw_money)
    EditText et_withdraw_money;
    @BindView(R.id.cb_user_agreement)
    CheckBox cb_user_agreement;

    private WithdrawContract.Presenter mPresenter;
    private String mBankCardId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        mPresenter = new WithdrawPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    private void initView() {
        tv_withdraw_max_money.setText(String.format("可提现余额%.2f元", getIntent().getSerializableExtra(KEY_BALANCE)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_BANK_CARD:
                if (resultCode == RESULT_OK && data != null) {
                    BankCardInfo bankCardInfo = data.getParcelableExtra(SelectBankCardActivity.KEY_BANK_CARD);
                    updateBankInfo(bankCardInfo);
                }
                break;
        }
    }

    @Override
    public void updateBankInfo(BankCardInfo bankCardInfo) {
        fresco_bank_logo.setImageURI("");
        if (bankCardInfo != null) {
            BankCardStyle bankCardStyle = bankCardInfo.getBankCardStyle();
            if (bankCardStyle != null) {
                ImageLoader.loadImageToView(bankCardStyle.getIconId(), fresco_bank_logo);
            }
            BankCard bankCard = bankCardInfo.getBankCard();
            if (bankCard != null) {
                mBankCardId = bankCard.getId();
                tv_bank.setText(bankCard.getOwnedBank() + bankCard.getCardType());
                tv_bank_number.setText(String.format("尾号%s", bankCard.getCardNum().replace("*", "")));
            }
        }
        fl_add_bank_card.setVisibility(View.GONE);
        rl_bank.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.tv_withdraw_explain, R.id.fl_add_bank_card, R.id.rl_bank, R.id.tv_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_withdraw_explain: {
                Intent intent = new Intent(context, ExplainActivity.class);
                intent.putExtra(ExplainActivity.KEY_TYPE, ExplainActivity.TYPE_WITHDRAW);
                startActivity(intent);
                break;
            }
            case R.id.fl_add_bank_card: {
                Intent intent = new Intent();
                if ("YES".equals(mPresenter.getIsSetPayPassword())) {
                    intent.setClass(context, AddBankCardPasswordActivity.class);
                    intent.putExtra(AddBankCardPasswordActivity.KEY_TYPE, AddBankCardPasswordActivity.TYPE_ADD_BANK_CARD);
                } else {
                    intent.setClass(context, AddBankCardActivity.class);
                }
                startActivity(intent);
                break;
            }
            case R.id.rl_bank: {
                Intent intent = new Intent(context, SelectBankCardActivity.class);
                intent.putParcelableArrayListExtra(SelectBankCardActivity.KEY_BANK_CARD_LIST, (ArrayList<BankCardInfo>) mPresenter.getBankCardList());
                intent.putExtra(SelectBankCardActivity.KEY_BANK_CARD_ID, mBankCardId);
                startActivityForResult(intent, REQUEST_SELECT_BANK_CARD);
                break;
            }
            case R.id.tv_withdraw:
                startActivity(new Intent(context, WithdrawPasswordActivity.class));
                break;
        }
    }
}
