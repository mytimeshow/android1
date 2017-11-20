package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import cn.czyugang.tcg.client.modules.balance.contract.RechargeContract;
import cn.czyugang.tcg.client.modules.balance.dialog.SelectPayTypeDialog;
import cn.czyugang.tcg.client.modules.balance.presenter.RechargePresenter;
import cn.czyugang.tcg.client.utils.ImageLoader;

/**
 * Created by wuzihong on 2017/10/16.
 * 充值
 */

public class RechargeActivity extends BaseActivity implements RechargeContract.View,
        SelectPayTypeDialog.OnSelectPayTypeListener {
    @BindView(R.id.fresco_bank_logo)
    SimpleDraweeView fresco_bank_logo;
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @BindView(R.id.tv_bank_number)
    TextView tv_bank_number;
    @BindView(R.id.tv_recharge_max_money)
    TextView tv_recharge_max_money;
    @BindView(R.id.et_recharge_money)
    EditText et_recharge_money;
    @BindView(R.id.cb_user_agreement)
    CheckBox cb_user_agreement;

    private RechargeContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        mPresenter = new RechargePresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        selectWX();
    }

    private void selectWX() {
        fresco_bank_logo.setImageURI("res:///" + R.drawable.ic_recharge_wx);
        tv_bank.setText("微信支付");
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_bank
                .getLayoutParams();
        lp.bottomMargin = 0;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        tv_bank_number.setVisibility(View.GONE);
    }

    private void selectAlipay() {
        fresco_bank_logo.setImageURI("res:///" + R.drawable.ic_recharge_alipay);
        tv_bank.setText("支付宝");
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_bank
                .getLayoutParams();
        lp.bottomMargin = 0;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        tv_bank_number.setVisibility(View.GONE);
    }

    private void selectRechargeChad() {
        fresco_bank_logo.setImageURI("res:///" + R.drawable.ic_recharge_recharge_card);
        tv_bank.setText("充值卡");
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_bank
                .getLayoutParams();
        lp.bottomMargin = 0;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        tv_bank_number.setVisibility(View.GONE);
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
                tv_bank.setText(bankCard.getOwnedBank() + bankCard.getCardType());
                tv_bank_number.setText(String.format("尾号%s", bankCard.getCardNum().replace("*", "")));
            }
        }
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tv_bank
                .getLayoutParams();
        lp.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.very_small);
        lp.bottomToBottom = R.id.guideline;
        tv_bank_number.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.rl_bank, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_bank:
                SelectPayTypeDialog.newInstance()
                        .setRankCardList(mPresenter.getBankCardList(), mPresenter.getIsSetPayPassword())
                        .setOnSelectPayTypeListener(this)
                        .show(getSupportFragmentManager(), "SelectPayTypeDialog");
                break;
            case R.id.tv_next:
                startActivity(new Intent(context, RechargePasswordActivity.class));
                break;
        }
    }

    @Override
    public void OnSelectPayType(SelectPayTypeDialog dialog, int type, Object data) {
        switch (type) {
            case SelectPayTypeDialog.WX:
                selectWX();
                break;
            case SelectPayTypeDialog.ALIPAY:
                selectAlipay();
                break;
            case SelectPayTypeDialog.RECHARGE_CARD:
                selectRechargeChad();
                break;
            case SelectPayTypeDialog.BANK_CARD:
                updateBankInfo((BankCardInfo) data);
                break;
        }
    }
}
