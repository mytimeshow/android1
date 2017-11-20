package cn.czyugang.tcg.client.modules.balance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.entity.Bill;
import cn.czyugang.tcg.client.modules.balance.adapter.BillAdapter;
import cn.czyugang.tcg.client.modules.balance.contract.BillContract;
import cn.czyugang.tcg.client.modules.balance.presenter.BillPresenter;

/**
 * Created by wuzihong on 2017/10/18.
 * 收支明细
 */

public class BillActivity extends BaseActivity implements BillContract.View,
        BaseRecyclerAdapter.OnItemClickListener {
    public static final String KEY_BALANCE = "balance";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    TextView tv_balance;
    TextView tv_income;
    TextView tv_expense;
    private BillAdapter mAdapter;

    private BillContract.Presenter mPresenter;

    private BigDecimal mBalanceDecimal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        mBalanceDecimal = (BigDecimal) getIntent().getSerializableExtra(KEY_BALANCE);
        mPresenter = new BillPresenter(this);
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new BillAdapter(context);
        View header_bill = LayoutInflater.from(context).inflate(R.layout.header_bill, recyclerView, false);
        mAdapter.addHeaderView(header_bill);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        tv_balance = (TextView) header_bill.findViewById(R.id.tv_balance);
        tv_income = (TextView) header_bill.findViewById(R.id.tv_income);
        tv_expense = (TextView) header_bill.findViewById(R.id.tv_expense);

        if (mBalanceDecimal != null) {
            tv_balance.setText(mBalanceDecimal.setScale(2, RoundingMode.HALF_UP).toString());
        }
    }

    @Override
    public void updateBillList(List<Bill> billList, boolean isMore) {
        mAdapter.setData(billList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateIncomeExpense(double income, double expense) {
        tv_income.setText(String.format("+%.2f", new BigDecimal(income)));
        tv_expense.setText(String.format("-%.2f", new BigDecimal(expense)));
    }

    @OnClick({R.id.iv_back, R.id.tv_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_filter:
                startActivity(new Intent(context, BillFilterActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object data) {
        startActivity(new Intent(context, BillDetailsActivity.class));
    }
}
