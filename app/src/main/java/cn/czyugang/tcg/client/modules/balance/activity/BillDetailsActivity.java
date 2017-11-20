package cn.czyugang.tcg.client.modules.balance.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.modules.balance.contract.BillDetailsContract;
import cn.czyugang.tcg.client.modules.balance.presenter.BillDetailsPresenter;
import cn.czyugang.tcg.client.utils.string.DateFormat;

/**
 * Created by wuzihong on 2017/10/18.
 * 账单详情
 */

public class BillDetailsActivity extends BaseActivity implements BillDetailsContract.View {
    public static final String KEY_BILL_ID = "billId";
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_bank)
    TextView tv_bank;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_number)
    TextView tv_number;

    private BillDetailsContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        ButterKnife.bind(this);
        mPresenter = new BillDetailsPresenter(this, getIntent().getStringExtra(KEY_BILL_ID));
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void updateBill(Bill bill) {
        if (bill != null) {
            tv_money.setText(new BigDecimal(bill.getTradePrice()).setScale(2, RoundingMode.HALF_UP).toString());
            tv_type.setText(bill.getActionTypeStr());
            tv_time.setText(DateFormat.format(bill.getCreateTime()));
            tv_number.setText(bill.getTradeNo());
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
