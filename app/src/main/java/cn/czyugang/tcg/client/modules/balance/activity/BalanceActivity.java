package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Balance;
import cn.czyugang.tcg.client.entity.BalanceInfo;
import cn.czyugang.tcg.client.modules.balance.contract.BalanceContract;
import cn.czyugang.tcg.client.modules.balance.presenter.BalancePresenter;

/**
 * Created by wuzihong on 2017/10/16.
 * 我的资产
 */

public class BalanceActivity extends BaseActivity implements BalanceContract.View {
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_withdraw_balance)
    TextView tv_withdraw_balance;
    @BindView(R.id.tv_bank_count)
    TextView tv_bank_count;
    @BindView(R.id.tv_discount_coupon_count)
    TextView tv_discount_coupon_count;
    @BindView(R.id.tv_points)
    TextView tv_points;

    private BalanceContract.Presenter mPresenter;

    private BigDecimal mBalanceDecimal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
        mPresenter = new BalancePresenter(this);
        mPresenter.subscribe();
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
    public void updateBalanceInfo(BalanceInfo balanceInfo) {
        if (balanceInfo != null) {
            Balance balance = balanceInfo.getBalance();
            if (balance != null) {
                BigDecimal withdrawBalanceDecimal = new BigDecimal(balance.getCashPrice()).add(new BigDecimal(balance.getFreezeCashPrice()));
                mBalanceDecimal = withdrawBalanceDecimal
                        .add(new BigDecimal(balance.getUncashPrice()))
                        .add(new BigDecimal(balance.getFreezeUncashPrice()));
                tv_balance.setText(mBalanceDecimal.setScale(2, RoundingMode.HALF_UP).toString());
                tv_withdraw_balance.setText(String.format("可提现余额%.2f", withdrawBalanceDecimal));
            }
            tv_bank_count.setText(String.format("已添加%d张卡", balanceInfo.getBankCardCount()));
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_recharge, R.id.tv_withdraw, R.id.tv_bill,
            R.id.fl_bank_card_manage, R.id.fl_discount_coupon, R.id.tv_coupon, R.id.fl_points,
            R.id.tv_red_packet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(context, RechargeActivity.class));
                break;
            case R.id.tv_withdraw: {
                Intent intent = new Intent(context, WithdrawActivity.class);
                BalanceInfo balanceInfo = mPresenter.getBalanceInfo();
                if (balanceInfo != null) {
                    Balance balance = balanceInfo.getBalance();
                    if (balance != null) {
                        intent.putExtra(WithdrawActivity.KEY_BALANCE, new BigDecimal(balance.getCashPrice()).add(new BigDecimal(balance.getFreezeCashPrice())));
                    }
                }
                startActivity(intent);
                break;
            }
            case R.id.tv_bill: {
                Intent intent = new Intent(context, BillActivity.class);
                intent.putExtra(BillActivity.KEY_BALANCE, mBalanceDecimal);
                startActivity(intent);
                break;
            }
            case R.id.fl_bank_card_manage:
                startActivity(new Intent(context, BankCardManageActivity.class));
                break;
            case R.id.fl_discount_coupon:
                break;
            case R.id.tv_coupon:
                break;
            case R.id.fl_points:
                break;
            case R.id.tv_red_packet:
                break;
        }
    }
}
